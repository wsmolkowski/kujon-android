package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.fragments.PlanFragment;

public class LecturerPlanActivity extends BaseActivity {

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;

    public static final String LECTURER_ID = "LECTURER_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_plan);
        ButterKnife.bind(this);
        toolbarTitle.setText(R.string.lecturers_plan);
        String lecturerId = getIntent().getStringExtra(LECTURER_ID);
        showFragment(PlanFragment.newLecturerPlanInstance(lecturerId), false);
    }

    public static void showLecturerPlan(Activity activity, String lecturerId) {
        Intent intent = new Intent(activity, LecturerPlanActivity.class);
        intent.putExtra(LECTURER_ID, lecturerId);
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
        toolbarTitle.setText(title);
    }

}
