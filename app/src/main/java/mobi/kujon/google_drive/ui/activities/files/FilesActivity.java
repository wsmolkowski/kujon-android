package mobi.kujon.google_drive.ui.activities.files;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.google_drive.dagger.injectors.FileActivityInjector;
import mobi.kujon.google_drive.dagger.injectors.FilesListFragmentInjector;
import mobi.kujon.google_drive.dagger.injectors.Injector;
import mobi.kujon.google_drive.model.dto.file_stream.FileUpdateDto;
import mobi.kujon.google_drive.model.dto.file_upload.FileUploadDto;
import mobi.kujon.google_drive.model.dto.file_upload_info.FileUploadInfoDto;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.mvp.file_stream_update.FileStreamUpdateMVP;
import mobi.kujon.google_drive.mvp.files_list.FileListMVP;
import mobi.kujon.google_drive.mvp.files_list.FilesOwnerType;
import mobi.kujon.google_drive.mvp.upload_file.UploadFileMVP;
import mobi.kujon.google_drive.services.ServiceOpener;
import mobi.kujon.google_drive.ui.activities.BaseFileActivity;
import mobi.kujon.google_drive.ui.activities.file_details.FileDetailsActivity;
import mobi.kujon.google_drive.ui.custom.UploadLayout;
import mobi.kujon.google_drive.ui.dialogs.choose_file_source.ChooseFileSourceDialog;
import mobi.kujon.google_drive.ui.dialogs.share_target.ShareTargetDialog;
import mobi.kujon.google_drive.ui.fragments.files.FilesListFragment;
import mobi.kujon.google_drive.ui.util.AbstractPageSelectedListener;
import mobi.kujon.google_drive.utils.GetFilePath;
import mobi.kujon.google_drive.utils.GoogleDriveFileException;
import mobi.kujon.google_drive.utils.GoogleDriveVirtualFile;
import mobi.kujon.google_drive.utils.PermissionAsk;
import mobi.kujon.google_drive.utils.TempFileCreator;


public class FilesActivity extends BaseFileActivity implements FileActivityView {

    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 539;
    private static final int REQUEST_CODE_OPENER = 1;
    public static final String COURSE_ID_KEY = "COURSE_ID_KEY";
    public static final String COURSE_NAME_KEY = "COURSE_NAME_KEY";
    public static final String TERM_ID_KEY = "TERM_ID_KEY";
    public static final int STORAGE_PERSMISSION = 324;
    private static final int FILE_RESULT_CODE = 789;
    public static final String CHOOSE_SOURCE = "Choose_SOURCE";
    public static final int FILE_DETAILS_RESULT_CODE = 874;
    private static final int REQUEST_CODE_FOR_FOLDER = 1234;
    private static final int GET_ACCOUNT_PERMISSION = 107;
    private static final int PURPOSE_DOWLOAD = 1;
    private static final int PURPOSE_ASK = 2;
    private FileActivityInjector fileActivityInjector;
    private FilesFragmentPagerAdapter adapter;
    private FileUploadInfoDto fileToUploadId;
    private File file;
    private boolean fileChoosen;
    private boolean deleteFileAfterUpload = false;

    public static void openActivity(Activity context, String courseId, String termId, String courseName, int requestCode) {
        Intent intent = new Intent(context, FilesActivity.class);
        intent.putExtra(COURSE_ID_KEY, courseId);
        intent.putExtra(TERM_ID_KEY, termId);
        intent.putExtra(COURSE_NAME_KEY, courseName);
        context.startActivityForResult(intent, requestCode);
    }

    private GoogleApiClient apiClient;
    private DriveId mSelectedFileDriveId;


    private String coursId, termId;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @BindView(R.id.update_layout)
    UploadLayout uploadLayout;


    @Inject
    FileListMVP.Model filesModel;

    @Inject
    FileStreamUpdateMVP.Presenter presenter;

    @Inject
    ServiceOpener serviceOpener;

    @Inject
    FileStreamUpdateMVP.CancelModel cancelModel;

    @Inject
    FileListMVP.DeletePresenter deletePresenter;

    @Inject
    UploadFileMVP.Presenter uploadPresenter;

    @Inject
    FileStreamUpdateMVP.CancelPresenter cancelPresenter;

    @Inject
    TempFileCreator tempFileCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coursId = getIntent().getStringExtra(COURSE_ID_KEY);
        termId = getIntent().getStringExtra(TERM_ID_KEY);
        String courseName = getIntent().getStringExtra(COURSE_NAME_KEY);
        handleInjections();
        setContentView(R.layout.activity_files);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbarTitle.setText(courseName);
        String[] titles = {getString(R.string.all_files), getString(R.string.my_files)};
        setUpViewPager(titles);
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        tabLayout.setupWithViewPager(viewPager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        doOnAllPermisions(this::askForFileOnDevice);
                    } else {
                        ChooseFileSourceDialog chooseFileSourceDialog = ChooseFileSourceDialog.newInstance();
                        chooseFileSourceDialog.setUpListener(this);
                        chooseFileSourceDialog.show(getSupportFragmentManager(), CHOOSE_SOURCE);
                    }
                }
        );
        uploadLayout.setUpdateFileListener(text -> {
            filesModel.load(true);
            this.setResult(RESULT_OK);
            this.onFileUploaded(text);
        });
        this.uploadLayout.setCancelModel(cancelModel);
        apiClient.connect();
        presenter.subscribeToStream(this);
        cancelPresenter.subscribeToStream(fileName -> {
            if (file != null && fileName.equals(file.getName())) {
                this.presenter.clearSubscriptions();
            }
        });
        filesModel.load(false);
    }

    private void setUpViewPager(String[] titles) {
        FilesListFragment[] fragments = {FilesListFragment.newInstance(FilesOwnerType.ALL), FilesListFragment.newInstance(FilesOwnerType.MY)};
        adapter = new FilesFragmentPagerAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new AbstractPageSelectedListener() {
            @Override
            public void onPageSelected(int position) {
                invalidateFragmentMenus(position);
            }
        });
        invalidateFragmentMenus(viewPager.getCurrentItem());
    }

    private void invalidateFragmentMenus(int position) {
        for (int i = 0; i < adapter.getCount(); i++) {
            adapter.getItem(i).setHasOptionsMenu(i == position);
        }
        invalidateOptionsMenu();
    }

    private void startFileSearching() {
        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setActivityTitle(getString(R.string.choose_file_to_send_to_kujon))
                .build(apiClient);
        try {
            startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.w("GOOGLE_DRIVE", "Unable to send intent", e);
        }
    }

    private void handleInjections() {
        fileActivityInjector = ((KujonApplication) getApplication()).getInjectorProvider().provideFileActivityInjector();
        fileActivityInjector.inject(this);
    }


    public String getCoursId() {
        return coursId;
    }

    public String getTermId() {
        return termId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        filesModel.clear();
        fileActivityInjector = null;
        presenter.clearSubscriptions();
        cancelPresenter.clearSubscriptions();
        uploadPresenter.clearSubscriptions();
    }

    @Override
    public Injector<FilesListFragment> provideInjector() {
        return new FilesListFragmentInjector(fileActivityInjector.getFilesActivityComponent());
    }


    @Override
    protected void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    apiClient.connect();
                }
                break;
            case REQUEST_CODE_OPENER:
                if (resultCode == RESULT_OK) {
                    handleGoodResponseFile(data);
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
            case REQUEST_CODE_FOR_FOLDER: {
                if (resultCode == RESULT_OK) {
                    openToUploadFileToDrive(data);
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
            break;
            case FILE_RESULT_CODE: {
                if (resultCode == RESULT_OK) {
                    handleResponseFromFile(data);
                }
            }
            break;
            case FILE_DETAILS_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    filesModel.load(true);
                }
                break;
        }
    }

    private void handleResponseFromFile(Intent data) {
        Uri uriToFile = data.getData();
        String path;
        try {
            path = GetFilePath.getPath(this, uriToFile);
            if (path != null) {
                file = new File(path);
                fileChoosen = true;
                showChooseStudentsDialog(file.getName());
                deleteFileAfterUpload = false;
            }
        } catch (GoogleDriveFileException e) {
            if (GoogleDriveVirtualFile.isVirtualFile(this, uriToFile)) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.warining)
                        .setMessage(R.string.this_file_will_be_saved_as_pdf)
                        .setPositiveButton(R.string.one_more_time, (dialog, which) -> {
                            dialog.dismiss();
                            startFileSearching();
                        })
                        .setNegativeButton(R.string.pdf_file, (dialog, which) -> {
                            saveFileOnKujonFromGoogleDrive(uriToFile);
                            dialog.dismiss();
                        }).show();
            } else {
                saveFileOnKujonFromGoogleDrive(uriToFile);
            }

        }
    }

    private void saveFileOnKujonFromGoogleDrive(Uri uriToFile) {
        try {
            Cursor returnCursor =
                    getContentResolver().query(uriToFile, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String fileName = returnCursor.getString(nameIndex);
            returnCursor.close();
            if (GoogleDriveVirtualFile.isVirtualFile(this, uriToFile) && !fileName.endsWith(".pdf")) {
                fileName = fileName.concat(".pdf");
            }
            InputStream inputStream;
            inputStream = GoogleDriveVirtualFile.getInputStream(this, uriToFile);
            file = tempFileCreator.writeToTempFile(inputStream, fileName);
            fileChoosen = true;
            deleteFileAfterUpload = true;
            showChooseStudentsDialog(file.getName());
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    private void openToUploadFileToDrive(Intent data) {
        serviceOpener.openAddToDriveService(fileToUploadId, data.getParcelableExtra(
                OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID));
    }

    private void handleGoodResponseFile(Intent data) {
        mSelectedFileDriveId = data.getParcelableExtra(
                OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
        fileChoosen = false;
        showChoiceDialog();
    }

    private void showChoiceDialog() {
        mSelectedFileDriveId.asDriveFile()
                .getMetadata(apiClient)
                .setResultCallback(metadataResult ->
                        showChooseStudentsDialog(metadataResult.getMetadata().getTitle()));
    }

    private void showChooseStudentsDialog(String fileTitle) {
        ShareTargetDialog dialog = ShareTargetDialog.newInstance(coursId, termId, fileTitle);
        dialog.show(getFragmentManager(), ShareTargetDialog.NAME);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        switch (purposeState) {
            case PURPOSE_DOWLOAD:
                purposeState = 0;
                askForFolder();
                break;
            case PURPOSE_ASK:
                purposeState = 0;
                doOnAllPermisions(this::askForFolder);
                break;
            default:
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
        }
    }

    @Override
    public void onUpdate(FileUpdateDto fileUpdateDto) {
        this.uploadLayout.update(fileUpdateDto);
    }

    @Override
    public void shareWith(@ShareFileTargetType String targetType, List<String> chosenStudentIds) {
        if (fileChoosen) {
            uploadPresenter.uploadFile(file, new FileUploadDto(coursId, termId, targetType, chosenStudentIds), deleteFileAfterUpload);
        } else {
            FileUploadDto fileUploadDto = new FileUploadDto(coursId, termId, targetType, chosenStudentIds);
            serviceOpener.openUploadService(fileUploadDto, mSelectedFileDriveId);
        }
    }

    @Override
    protected void setLoading(boolean t) {

    }

    @Override
    public void onFileDelete(String fileId, String name) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.you_want_to_delete_this_file)
                .setTitle(R.string.are_you_sure)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    dialog.dismiss();
                    deletePresenter.deleteFile(fileId, name);
                    this.setLoading(true);
                }).setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onFileAddToGoogleDrive(FileUploadInfoDto fileId) {
        dowloadFile = false;
        this.fileToUploadId = fileId;
        if (PermissionAsk.askForPermission(this, getString(R.string.get_account_permission), GET_ACCOUNT_PERMISSION)) {
            doOnAllPermisions(this::askForFolder);
        }
    }

    private void doOnAllPermisions(LamdaFunciton lamdaFunciton) {
        if (PermissionAsk.askForPermission(this, getString(R.string.storage_permission), STORAGE_PERSMISSION) &&
                PermissionAsk.askForPermission(this, getString(R.string.storage__read_permission), STORAGE_PERSMISSION)) {
            lamdaFunciton.doIt();
        }
    }

    private void askForFolder() {
        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setActivityTitle(getString(R.string.choose_folder))
                .setMimeType(new String[]{DriveFolder.MIME_TYPE})
                .build(apiClient);
        try {
            startIntentSenderForResult(intentSender, REQUEST_CODE_FOR_FOLDER, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            Log.w("GOOGLE_DRIVE", "Unable to send intent", e);
        }
    }

    private int purposeState = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERSMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (dowloadFile) {
                serviceOpener.openDowloadService(fileToUploadId);
            } else {
                if (reconectGoogleClient(PURPOSE_DOWLOAD)) {
                    askForFolder();
                }
            }
        } else if (requestCode == GET_ACCOUNT_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (reconectGoogleClient(PURPOSE_ASK)) {
                doOnAllPermisions(this::askForFolder);
            }
        }
    }

    private boolean reconectGoogleClient(int state) {
        if (apiClient != null && !apiClient.isConnected()) {
            purposeState = state;
            apiClient.reconnect();
            return false;
        }
        return true;
    }

    @Override
    public void onFileDetails(String fileId) {
        FileDetailsActivity.openActivity(this, coursId, termId, fileId, FILE_DETAILS_RESULT_CODE);
    }

    private boolean dowloadFile = false;

    @Override
    public void onDownload(FileUploadInfoDto file) {
        dowloadFile = true;
        this.fileToUploadId = file;
        doOnAllPermisions(() -> serviceOpener.openDowloadService(file));

    }

    @Override
    public void fileDeleted(String name) {
        this.setLoading(false);
        this.setResult(RESULT_OK);
        filesModel.load(false);
        filesModel.load(true);
        Toast.makeText(this, String.format("%s %s", name, getString(R.string.file_deleted)), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGoogleDriveChoose() {
        startFileSearching();
    }

    @Override
    public void onDeviceFileChoose() {
        doOnAllPermisions(this::askForFileOnDevice);
    }

    private void askForFileOnDevice() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.get_file)), FILE_RESULT_CODE);
    }

    @Override
    public void onFileUploaded(String text) {
        Toast toast = Toast.makeText(this, String.format("%s %s", text, getString(R.string.file_added)), Toast.LENGTH_SHORT);
        toast.show();
    }
}
