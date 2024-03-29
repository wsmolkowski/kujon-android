package mobi.kujon.google_drive.ui.activities.file_details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.google_drive.dagger.injectors.FileDetailsInjector;
import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file_details.FileDetailsDto;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsMVP;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsView;
import mobi.kujon.google_drive.ui.activities.BaseFileActivity;
import mobi.kujon.google_drive.ui.activities.file_details.recycler_classes.FileDetailsAdapter;

public class FileDetailsActivity extends BaseFileActivity implements FileDetailsView, FileDetailsAdapter.OnEveryoneSwitchClicked {

    private String coursId;
    private String termId;
    private String fileId;
    private boolean everyoneChosenToShare;
    public static final String COURSE_ID_KEY = "COURSE_ID";
    public static final String TERM_ID_KEY = "TERM_ID";
    public static final String FILE_ID_KEY = "FILE_ID";


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cancel)
    ImageView cancel;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    FileDetailsMVP.FileDetailsPresenter fileDetailsPresenter;

    @Inject
    FileDetailsMVP.ShareFilePresenter shareFilePresenter;


    private FileDetailsAdapter adapter;
    private boolean close;

    public static void openActivity(Activity context, String courseId, String termId, String fileId, int code) {
        Intent intent = new Intent(context, FileDetailsActivity.class);
        intent.putExtra(COURSE_ID_KEY, courseId);
        intent.putExtra(TERM_ID_KEY, termId);
        intent.putExtra(FILE_ID_KEY, fileId);
        context.startActivityForResult(intent, code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coursId = getIntent().getStringExtra(COURSE_ID_KEY);
        termId = getIntent().getStringExtra(TERM_ID_KEY);
        fileId = getIntent().getStringExtra(FILE_ID_KEY);
        handleInjection();
        setContentView(R.layout.activity_file_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(R.string.file_details);
        handleRecyclerView();
        fileDetailsPresenter.loadFileDetails(fileId, false);
        this.setLoading(true);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fileDetailsPresenter.loadFileDetails(fileId, true);
        });
        cancel.setOnClickListener(v -> {
            if (checkForChanges()) return;
            finish();
        });
    }

    private void handleRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new FileDetailsAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.file_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_change_share_file && adapter.isChanged()) {
            close = true;
            saveChanges();
            return true;
        }
        return false;
    }

    private void saveChanges() {
        shareFilePresenter.shareFileWith(fileId, selectTargetType(), adapter.getStudentShareDTOs());
        this.setLoading(true);
    }


    @Override
    public void onBackPressed() {
        if (checkForChanges()) return;
        super.onBackPressed();

    }

    private boolean checkForChanges() {
        if (adapter.isChanged()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.warining)
                    .setMessage(R.string.unsaved_changes)
                    .setNegativeButton(R.string.discard, (dialog, which) -> {
                        dialog.dismiss();
                        this.finish();
                    })
                    .setPositiveButton(R.string.save_changes, (dialog, which) -> {
                        saveChanges();
                        close = true;
                    }).show();
            return true;
        }
        return false;
    }

    private
    @ShareFileTargetType
    String selectTargetType() {
        if (everyoneChosenToShare) {
            return ShareFileTargetType.ALL;
        } else {
            for (StudentShareDto studentShareDTO : adapter.getStudentShareDTOs()) {
                if (studentShareDTO.isChosen()) {
                    return ShareFileTargetType.LIST;
                }
            }

            return ShareFileTargetType.NONE;
        }
    }

    public String getCoursId() {
        return coursId;
    }

    public String getTermId() {
        return termId;
    }


    @Override
    public void displayFileDetails(FileDetailsDto fileDetailsDto) {
        adapter.setFileDetailsDto(fileDetailsDto);
        this.setLoading(false);
    }

    @Override
    public void fileShared(@ShareFileTargetType String shareType, List<String> fileSharedWith) {
        if (close) {
            this.setResult(RESULT_OK);
            finish();
        } else {
            adapter.setShareType(shareType, fileSharedWith);
            this.setResult(RESULT_OK);
            this.setLoading(false);
        }

    }


    @Override
    protected void setLoading(boolean t) {
        this.swipeRefreshLayout.setRefreshing(t);
    }


    public void handleInjection() {
        FileDetailsInjector injector = (FileDetailsInjector) ((KujonApplication) getApplication())
                .getInjectorProvider().provideFileDetailsActivityInjector();
        injector.inject(this);
    }

    @Override
    public void onEveryoneClicked(boolean isEveryone) {
        everyoneChosenToShare = isEveryone;
        this.setLoading(true);
        shareFilePresenter.shareFileWith(fileId, isEveryone ? ShareFileTargetType.ALL : ShareFileTargetType.LIST, adapter.getStudentShareDTOs());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareFilePresenter.clearSubscriptions();
        fileDetailsPresenter.clearSubscriptions();
    }

    @Override
    public void shareFailed() {
        adapter.notifyDataSetChanged();
    }
}
