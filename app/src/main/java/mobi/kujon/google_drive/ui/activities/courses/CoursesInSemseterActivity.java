package mobi.kujon.google_drive.ui.activities.courses;

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
import mobi.kujon.google_drive.model.dto.CourseDTO;
import mobi.kujon.google_drive.model.dto.TermWithCourseDTO;
import mobi.kujon.google_drive.mvp.courses_list.CoursesMVP;
import mobi.kujon.google_drive.ui.activities.BaseFileActivity;
import mobi.kujon.google_drive.ui.activities.courses.recycler_classes.CoursesAdapter;
import mobi.kujon.google_drive.ui.activities.files.FilesActivity;
import mobi.kujon.google_drive.ui.util.OnDtoClick;

public class CoursesInSemseterActivity extends BaseFileActivity implements CoursesMVP.View, OnDtoClick<CourseDTO> {

    private CoursesAdapter adapter;

    public static void openCourseInSemester(Activity activity){
        Intent intent = new Intent(activity,CoursesInSemseterActivity.class);
        activity.startActivity(intent);
    }

    @Inject
    CoursesMVP.Presenter presenter;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;

    @Bind(R.id.coursesRecyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((KujonApplication)getApplication()).getInjectorProvider().provideCourseInSemesterInjector().inject(this);
        setContentView(R.layout.activity_courses_in_semseter);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle.setText(R.string.shared_files_title);

        presenter.loadTermsInCourses(false);
        swipeRefreshLayout.setOnRefreshListener(() -> this.presenter.loadTermsInCourses(true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CoursesAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected void setLoading(boolean t) {
        swipeRefreshLayout.setRefreshing(false);
    }






    @Override
    public void onCoursesTermsLoaded(List<TermWithCourseDTO> courseDTOs) {
        adapter.setData(courseDTOs);
        setLoading(false);
    }

    @Override
    public void onDtoClick(CourseDTO courseDTO) {
        FilesActivity.openActivity(this,courseDTO.getCourseId(),courseDTO.getTermId());
    }
}
