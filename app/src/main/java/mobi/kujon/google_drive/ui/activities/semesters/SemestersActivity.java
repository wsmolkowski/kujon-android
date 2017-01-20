package mobi.kujon.google_drive.ui.activities.semesters;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.List;

import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.google_drive.model.dto.SemesterDTO;
import mobi.kujon.google_drive.mvp.semester_list.SemestersMVP;
import mobi.kujon.google_drive.ui.activities.semesters.dagger.SemesterActivityComponent;


public class SemestersActivity extends AppCompatActivity implements SemestersMVP.View {


    private SemesterActivityComponent semesterActivityComponent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((KujonApplication)this.getApplication()).getInjectorProvider().provideInjector().inject(this);
        setContentView(R.layout.activity_semesters);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void semestersLoaded(List<SemesterDTO> list) {

    }

    @Override
    public void handleException(Throwable throwable) {

    }
}
