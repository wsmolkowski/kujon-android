package mobi.kujon.google_drive.ui.activities.files;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.google_drive.dagger.injectors.FileActivityInjector;
import mobi.kujon.google_drive.dagger.injectors.FilesListFragmentInjector;
import mobi.kujon.google_drive.dagger.injectors.Injector;
import mobi.kujon.google_drive.ui.fragments.ProvideInjector;
import mobi.kujon.google_drive.ui.fragments.files.FilesListFragment;


public class FilesActivity extends AppCompatActivity implements ProvideInjector<FilesListFragment> {


    public static final String COURSE_ID_KEY = "COURSE_ID_KEY";
    public static final String TERM_ID_KEY = "TERM_ID_KEY";
    private FileActivityInjector fileActivityInjector;

    public static void openActivity(Context context, String courseId, String termId){
        Intent intent  = new Intent(context,FilesActivity.class);
        intent.putExtra(COURSE_ID_KEY,courseId);
        intent.putExtra(TERM_ID_KEY,termId);
        context.startActivity(intent);
    }



    private String coursId,termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coursId = getIntent().getStringExtra(COURSE_ID_KEY);
        termId = getIntent().getStringExtra(COURSE_ID_KEY);
        fileActivityInjector = ((KujonApplication) getApplication()).getInjectorProvider().provideFileActivityInjector();
        fileActivityInjector.inject(this);
        setContentView(R.layout.activity_files);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
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
}
