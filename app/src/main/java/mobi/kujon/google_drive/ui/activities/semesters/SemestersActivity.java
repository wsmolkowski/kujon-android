package mobi.kujon.google_drive.ui.activities.semesters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.SemesterDTO;
import mobi.kujon.google_drive.mvp.semester_list.SemestersMVP;
import mobi.kujon.google_drive.ui.activities.BaseFileActivity;
import mobi.kujon.google_drive.ui.activities.semesters.recycler_classes.SemesterAdapter;
import mobi.kujon.google_drive.ui.util.OnDtoClick;


public class SemestersActivity extends BaseFileActivity implements SemestersMVP.View, OnDtoClick<SemesterDTO> {


    public static void openSemesterActivity(Activity activity){
        Intent intent = new Intent(activity,SemestersActivity.class);
        activity.startActivity(intent);
    }

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Inject
    SemestersMVP.Presenter presenter;
    private SemesterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((KujonApplication) this.getApplication()).getInjectorProvider().provideInjector().inject(this);
        setContentView(R.layout.activity_semesters);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(R.string.semester_choose);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new SemesterAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(true);
        presenter.askForSemesters(false);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.askForSemesters(true));
    }

    @Override
    public void semestersLoaded(List<SemesterDTO> list) {
        adapter.setSemesterDTOs(list);
        swipeRefreshLayout.setRefreshing(false);
    }



    @Override
    protected void setLoading(boolean t) {
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onDtoClick(SemesterDTO semesterDTO) {
//        CoursesInSemseterActivity.openCourseInSemester(this,semesterDTO.getSemesterId(),semesterDTO.getSemesterCode());
    }
}
