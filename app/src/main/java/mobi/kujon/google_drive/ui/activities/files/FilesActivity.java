package mobi.kujon.google_drive.ui.activities.files;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
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
import mobi.kujon.google_drive.services.ServiceOpener;
import mobi.kujon.google_drive.ui.activities.BaseFileActivity;
import mobi.kujon.google_drive.ui.activities.file_details.FileDetailsActivity;
import mobi.kujon.google_drive.ui.custom.UploadLayout;
import mobi.kujon.google_drive.ui.dialogs.file_info_dialog.FileActionListener;
import mobi.kujon.google_drive.ui.dialogs.share_target.ChooseShareStudentsListener;
import mobi.kujon.google_drive.ui.dialogs.share_target.ShareTargetDialog;
import mobi.kujon.google_drive.ui.fragments.ProvideInjector;
import mobi.kujon.google_drive.ui.fragments.files.FilesListFragment;
import mobi.kujon.google_drive.ui.util.AbstractPageSelectedListener;
import mobi.kujon.google_drive.utils.PermissionAsk;


public class FilesActivity extends BaseFileActivity implements ProvideInjector<FilesListFragment>,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        FileStreamUpdateMVP.View,
        ChooseShareStudentsListener, FileActionListener, FileListMVP.DeleteView {

    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 539;
    private static final int REQUEST_CODE_OPENER = 1;
    public static final String COURSE_ID_KEY = "COURSE_ID_KEY";
    public static final String TERM_ID_KEY = "TERM_ID_KEY";
    public static final int STORAGE_PERSMISSION = 324;
    private FileActivityInjector fileActivityInjector;
    private FilesFragmentPagerAdapter adapter;
    private FileUploadInfoDto fileToUploadId;
    private static final int REQUEST_CODE_FOR_FOLDER = 1234;

    public static void openActivity(Activity context, String courseId, String termId) {
        Intent intent = new Intent(context, FilesActivity.class);
        intent.putExtra(COURSE_ID_KEY, courseId);
        intent.putExtra(TERM_ID_KEY, termId);
        context.startActivity(intent);
    }

    private GoogleApiClient apiClient;
    private DriveId mSelectedFileDriveId;


    private String coursId, termId;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.sliding_tabs)
    TabLayout tabLayout;

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;

    @Bind(R.id.update_layout)
    UploadLayout uploadLayout;

    @Inject
    FileStreamUpdateMVP.Presenter presenter;

    @Inject
    ServiceOpener serviceOpener;

    @Inject
    FileStreamUpdateMVP.CancelModel cancelModel;

    @Inject
    FileListMVP.DeletePresenter deletePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coursId = getIntent().getStringExtra(COURSE_ID_KEY);
        termId = getIntent().getStringExtra(TERM_ID_KEY);
        handleInjections();
        setContentView(R.layout.activity_files);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbarTitle.setText(R.string.files_title);
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
                    startFileSearching();
                }
        );
        uploadLayout.setUpdateFileListener(() -> {
            this.adapter.refresh();
        });
        this.uploadLayout.setCancelModel(cancelModel);
        apiClient.connect();
        presenter.subscribeToStream(this);
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

    private void invalidateFragmentMenus(int position){
        for(int i = 0; i < adapter.getCount(); i++){
            adapter.getItem(i).setHasOptionsMenu(i == position);
        }
        invalidateOptionsMenu(); //or respectively its support method.
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
        fileActivityInjector = null;
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
        }
    }

    private void openToUploadFileToDrive(Intent data) {
        serviceOpener.openAddToDriveService(fileToUploadId, data.getParcelableExtra(
                OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID));
    }

    private void handleGoodResponseFile(Intent data) {
        mSelectedFileDriveId = data.getParcelableExtra(
                OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
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
                // Unable to resolve, message user appropriately
            }
        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
        }
    }

    @Override
    public void onUpdate(FileUpdateDto fileUpdateDto) {
        Log.d(fileUpdateDto.getFileName(), String.format("Loading progress: %d percent", fileUpdateDto.getProgress()));
        this.uploadLayout.update(fileUpdateDto);
    }

    @Override
    public void shareWith(@ShareFileTargetType String targetType, List<String> chosenStudentIds) {
        FileUploadDto fileUploadDto = new FileUploadDto(coursId, termId, targetType, chosenStudentIds);
        serviceOpener.openUploadService(fileUploadDto, mSelectedFileDriveId);
    }

    @Override
    protected void setLoading(boolean t) {

    }

    @Override
    public void onFileDelete(String fileId) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.you_want_to_delete_this_file)
                .setTitle(R.string.are_you_sure)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    dialog.dismiss();
                    deletePresenter.deleteFile(fileId);
                    this.setLoading(true);
                }).setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss()).show();
    }

    @Override
    public void onFileAddToGoogleDrive(FileUploadInfoDto fileId) {
        this.fileToUploadId = fileId;
        doOnAllPermisions();

    }

    private void doOnAllPermisions() {
        if (PermissionAsk.askForPermission(this, getString(R.string.storage_permission), STORAGE_PERSMISSION) &&
                PermissionAsk.askForPermission(this, getString(R.string.storage__read_permission), STORAGE_PERSMISSION)) {
            askForFolder();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERSMISSION) {
            doOnAllPermisions();
        }
    }

    @Override
    public void onFileDetails(String fileId) {
        FileDetailsActivity.openActivity(this, coursId, termId, fileId);
    }

    @Override
    public void fileDeleted() {
        this.setLoading(false);
        this.adapter.refresh();
    }
}
