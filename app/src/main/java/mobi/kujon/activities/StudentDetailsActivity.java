package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.fragments.StudentInfoFragment;

public class StudentDetailsActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        ButterKnife.bind(this);
        String userId = getIntent().getStringExtra(StudentInfoFragment.USER_ID);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.placeholder, StudentInfoFragment.getFragment(userId));
        transaction.commit();
    }

    public static void showStudentDetails(Activity activity, String userId) {
        Intent intent = new Intent(activity, StudentDetailsActivity.class);
        intent.putExtra(StudentInfoFragment.USER_ID, userId);
        activity.startActivity(intent);
    }
    public void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }
}
