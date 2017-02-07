package mobi.kujon.google_drive.ui.activities.file_details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.google_drive.dagger.injectors.FileDetailsInjector;
import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file.FileDTO;
import mobi.kujon.google_drive.model.dto.file_details.DisableableStudentShareDTO;
import mobi.kujon.google_drive.model.dto.file_share.AskForStudentDto;
import mobi.kujon.google_drive.model.json.ShareFileTargetType;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsMVP;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsMVP;
import mobi.kujon.google_drive.mvp.file_details.FileDetailsView;
import mobi.kujon.google_drive.network.KujonException;
import mobi.kujon.google_drive.ui.activities.file_details.recycler_classes.FileDetailsAdapter;
import mobi.kujon.google_drive.ui.activities.files.FilesActivity;
import mobi.kujon.google_drive.ui.dialogs.share_target.ShareTargetDialog;
import mobi.kujon.utils.ErrorHandlerUtil;

public class FileDetailsActivity extends AppCompatActivity implements FileDetailsView, ChooseStudentsMVP.View, FileDetailsAdapter.OnEveryoneSwitchClicked{

    private String coursId;
    private String termId;
    private String fileId;
    private boolean everyoneChosenToShare;
    public static final String COURSE_ID_KEY = "COURSE_ID";
    public static final String TERM_ID_KEY = "TERM_ID";
    public static final String FILE_ID_KEY = "FILE_ID";


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.cancel)
    ImageView cancel;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    FileDetailsMVP.FileDetailsPresenter fileDetailsPresenter;

    @Inject
    FileDetailsMVP.ShareFilePresenter shareFilePresenter;

    @Inject
    FileDetailsMVP.StudentsPresenter studentsPresenter;

    private FileDetailsAdapter adapter;

    public static void openActivity(Activity context, String courseId, String termId, String fileId) {
        Intent intent = new Intent(context, FilesActivity.class);
        intent.putExtra(COURSE_ID_KEY, courseId);
        intent.putExtra(TERM_ID_KEY, termId);
        intent.putExtra(FILE_ID_KEY, fileId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coursId = getIntent().getStringExtra(COURSE_ID_KEY);
        termId = getIntent().getStringExtra(TERM_ID_KEY);
        fileId = getIntent().getStringExtra(FILE_ID_KEY);
        handleInejction();
        setContentView(R.layout.activity_file_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(getIntent().getStringExtra(ShareTargetDialog.TITLE));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new FileDetailsAdapter(new ArrayList<>(), this, null);
        recyclerView.setAdapter(adapter);
        fileDetailsPresenter.loadFileDetails(fileId, true);
        studentsPresenter.loadStudents(fileId, true);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fileDetailsPresenter.loadFileDetails(fileId, true);
            studentsPresenter.loadStudents(fileId, true);
        });
        cancel.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_file_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.share_file){
            shareFilePresenter.shareFileWith(fileId, selectTargetType(), adapter.getStudentShareDTOs());
            return true;
        }
        return false;
    }

    private @ShareFileTargetType String selectTargetType(){
        if(everyoneChosenToShare) {
            return ShareFileTargetType.ALL;
        } else {
            for(DisableableStudentShareDTO studentShareDTO : adapter.getStudentShareDTOs()) {
                if(studentShareDTO.isChosen()) {
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
    public void displayFileProperties(FileDTO fileDTO) {
        adapter.addFileDTO(fileDTO);
    }

    @Override
    public void fileShared() {
        finish();
    }

    @Override
    public void displayFileShares(List<DisableableStudentShareDTO> shares) {
        adapter.addStudents(shares);
    }

    @Override
    public void handleException(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        try {
            throw  throwable;
        }catch (KujonException e){
            ErrorHandlerUtil.handleKujonError(e);
        }catch (Throwable t){
            ErrorHandlerUtil.handleError(t);
        }
    }

    @Override
    public void showStudentList(List<StudentShareDto> studentShareDtos) {
    }

    public void handleInejction() {
        FileDetailsInjector injector = (FileDetailsInjector) ((KujonApplication) getApplication())
                .getInjectorProvider().provideFileDetailsActivityInjector();
        injector.inject(this);
    }

    @Override
    public void onEveryoneClicked(boolean isEveryone) {
        everyoneChosenToShare = isEveryone;
        studentsPresenter.chooseEveryoneToShare(isEveryone, adapter.getStudentShareDTOs());
    }
}
