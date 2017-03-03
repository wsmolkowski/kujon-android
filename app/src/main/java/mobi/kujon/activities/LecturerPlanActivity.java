package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.fragments.PlanFragment;

public class LecturerPlanActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public static final String LECTURER_ID = "LECTURER_ID";
    public static final String LECTURER_NAME = "LECTURER_NAME";
    private String text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_plan);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        String lecturerId = getIntent().getStringExtra(LECTURER_ID);
        String lecturerName = getIntent().getStringExtra(LECTURER_NAME);
        showFragment(PlanFragment.newLecturerPlanInstance(lecturerId), false);
        text = new StringBuilder().append(getText(R.string.lecturers_plan)).append(" ").append(lecturerName).toString();
        toolbarTitle.setText(text);
    }

    public static void showLecturerPlan(Activity activity, String lecturerId, String lecturerName) {
        Intent intent = new Intent(activity, LecturerPlanActivity.class);
        intent.putExtra(LECTURER_ID, lecturerId);
        intent.putExtra(LECTURER_NAME, lecturerName);
        activity.startActivity(intent);
    }


    private void showFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.place_holder, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public void setToolbarTitle(int title) {
        toolbarTitle.setText(text);
    }

}
