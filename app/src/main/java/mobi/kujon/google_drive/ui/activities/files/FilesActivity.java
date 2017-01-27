package mobi.kujon.google_drive.ui.activities.files;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.google_drive.dagger.injectors.FileActivityInjector;
import mobi.kujon.google_drive.dagger.injectors.FilesListFragmentInjector;
import mobi.kujon.google_drive.dagger.injectors.Injector;
import mobi.kujon.google_drive.mvp.files_list.FilesOwnerType;
import mobi.kujon.google_drive.ui.activities.BaseFileActivity;
import mobi.kujon.google_drive.ui.dialogs.ShareTargetDialog;
import mobi.kujon.google_drive.ui.fragments.ProvideInjector;
import mobi.kujon.google_drive.ui.fragments.files.FilesListFragment;


public class FilesActivity extends BaseFileActivity implements ProvideInjector<FilesListFragment> {


    public static final String COURSE_ID_KEY = "COURSE_ID_KEY";
    public static final String TERM_ID_KEY = "TERM_ID_KEY";
    private FileActivityInjector fileActivityInjector;

    public static void openActivity(Activity context, String courseId, String termId) {
        Intent intent = new Intent(context, FilesActivity.class);
        intent.putExtra(COURSE_ID_KEY, courseId);
        intent.putExtra(TERM_ID_KEY, termId);
        context.startActivity(intent);
    }


    private String coursId, termId;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.sliding_tabs)
    TabLayout tabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coursId = getIntent().getStringExtra(COURSE_ID_KEY);
        termId = getIntent().getStringExtra(TERM_ID_KEY);
        fileActivityInjector = ((KujonApplication) getApplication()).getInjectorProvider().provideFileActivityInjector();
        fileActivityInjector.inject(this);
        setContentView(R.layout.activity_files);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String[] titles = {getString(R.string.all_files), getString(R.string.my_files)};
        Fragment[] fragments = {FilesListFragment.newInstance(FilesOwnerType.ALL), FilesListFragment.newInstance(FilesOwnerType.MY)};
        viewPager.setAdapter(new FilesFragmentPagerAdapter(getSupportFragmentManager(), titles, fragments));

        tabLayout.setupWithViewPager(viewPager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
                    ShareTargetDialog shareTargetDialog = new ShareTargetDialog();
                    shareTargetDialog.show(getFragmentManager(), "tag");
                }
        );
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

    @Override
    protected void setLoading(boolean t) {

    }
}
