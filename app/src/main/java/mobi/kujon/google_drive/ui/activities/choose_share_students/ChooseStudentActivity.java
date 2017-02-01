package mobi.kujon.google_drive.ui.activities.choose_share_students;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.StudentShareDto;
import mobi.kujon.google_drive.mvp.HandleException;
import mobi.kujon.google_drive.mvp.choose_students.ChooseStudentsMVP;

public class ChooseStudentActivity extends AppCompatActivity implements HandleException, ChooseStudentsMVP.View {


    @Inject
    ChooseStudentsMVP.Presenter presenter;

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((KujonApplication) this.getApplication()).getInjectorProvider().provideChooseStudentActivityInjector().inject(this);
        setContentView(R.layout.activity_choose_students);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void handleException(Throwable throwable) {

    }

    @Override
    public void showStudentList(List<StudentShareDto> studentShareDtos) {

    }
}
