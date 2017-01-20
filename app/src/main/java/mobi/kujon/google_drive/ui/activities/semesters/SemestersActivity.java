package mobi.kujon.google_drive.ui.activities.semesters;

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


public class SemestersActivity extends BaseFileActivity implements SemestersMVP.View, SemesterAdapter.OnSemesterClick {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((KujonApplication) this.getApplication()).getInjectorProvider().provideInjector().inject(this);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_semesters);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(R.string.semester_choose);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new SemesterAdapter(new ArrayList<>(),this));
        swipeRefreshLayout.setRefreshing(true);
        presenter.askForSemesters(false);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.askForSemesters(true));
    }

    @Override
    public void semestersLoaded(List<SemesterDTO> list) {

    }

    @Override
    protected void setProgress(boolean t) {

    }

    @Override
    public void click(SemesterDTO semesterDTO) {

    }
}
