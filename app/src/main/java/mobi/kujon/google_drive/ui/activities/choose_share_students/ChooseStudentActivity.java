package mobi.kujon.google_drive.ui.activities.choose_share_students;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file_share.AskForStudentDto;
import mobi.kujon.google_drive.mvp.HandleException;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsMVP;
import mobi.kujon.google_drive.ui.activities.BaseFileActivity;
import mobi.kujon.google_drive.ui.activities.choose_share_students.recycler_classes.ChooseStudentAdapter;
import mobi.kujon.google_drive.ui.dialogs.share_target.ShareTargetDialog;

public class ChooseStudentActivity extends BaseFileActivity implements HandleException, ChooseStudentsMVP.View {

    public static final int CHOOSE_STUDENTS_REQUEST = 1;
    public static final String CHOOSE_STUDENTS_RESPONSE = "RESPONSE";
    public static final int CANCEL = 2;
    public static final int CHOSEN = 3;

    @Inject
    ChooseStudentsMVP.Presenter presenter;

    @BindView(R.id.cancel)
    ImageView cancel;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private ChooseStudentAdapter adapter;

    private String courseId;
    private String termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((KujonApplication) this.getApplication()).getInjectorProvider().provideChooseStudentActivityInjector().inject(this);
        setContentView(R.layout.activity_choose_students);
        ButterKnife.bind(this);
        courseId = getIntent().getStringExtra(ShareTargetDialog.COURSE_ID);
        termId = getIntent().getStringExtra(ShareTargetDialog.TERM_ID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(R.string.choose_student);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ChooseStudentAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        this.setLoading(true);
        presenter.loadListOfStudents(new AskForStudentDto(courseId, termId, null), false);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadListOfStudents(new AskForStudentDto(courseId, termId, null), true));
        cancel.setOnClickListener(v -> {
            setResult(CANCEL);
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
            Intent intent = new Intent();
            intent.putExtra(CHOOSE_STUDENTS_RESPONSE, getStudentIds());
            setResult(CHOSEN, intent);
            finish();
            return true;
        }
        return false;
    }


    private String[] getStudentIds() {
        List<StudentShareDto> dtos = adapter.getStudentsToShare();
        List<String> ids = new ArrayList<>();
        for(StudentShareDto dto : dtos) {
            if(dto.isChosen()) {
                ids.add(dto.getStudentId());
            }
        }
        return ids.toArray(new String[ids.size()]);
    }

    @Override
    public void showStudentList(List<StudentShareDto> studentShareDtos) {
        adapter.setStudentsToShare(studentShareDtos);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void setLoading(boolean t) {
        swipeRefreshLayout.setRefreshing(t);
    }
}
