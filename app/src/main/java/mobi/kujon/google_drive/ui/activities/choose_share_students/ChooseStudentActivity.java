package mobi.kujon.google_drive.ui.activities.choose_share_students;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.model.dto.file_share.AskForStudentDto;
import mobi.kujon.google_drive.mvp.HandleException;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsMVP;
import mobi.kujon.google_drive.network.KujonException;
import mobi.kujon.google_drive.ui.activities.choose_share_students.recycler_classes.ChooseStudentAdapter;
import mobi.kujon.google_drive.ui.dialogs.share_target.ShareTargetDialog;
import mobi.kujon.utils.ErrorHandlerUtil;

public class ChooseStudentActivity extends AppCompatActivity implements HandleException, ChooseStudentsMVP.View {

    public static final int CHOOSE_STUDENTS_REQUEST = 1;
    public static final String CHOOSE_STUDENTS_RESPONSE = "RESPONSE";
    public static final int CANCEL = 2;
    public static final int CHOSEN = 3;

    @Inject
    ChooseStudentsMVP.Presenter presenter;

    @Bind(R.id.cancel)
    ImageView cancel;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.share)
    TextView share;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refresh)
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
        toolbarTitle.setText(getIntent().getStringExtra(ShareTargetDialog.TITLE));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ChooseStudentAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(true);
        presenter.loadListOfStudents(new AskForStudentDto(courseId, termId, null), false);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadListOfStudents(new AskForStudentDto(courseId, termId, null), true));
        cancel.setOnClickListener(v -> {
            setResult(CANCEL);
            finish();
        });
        share.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(CHOOSE_STUDENTS_RESPONSE, getStudentIds());
            setResult(CHOSEN, intent);
            finish();
        });
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
}
