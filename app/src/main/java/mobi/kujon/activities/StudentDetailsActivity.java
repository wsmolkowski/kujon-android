package mobi.kujon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import mobi.kujon.R;
import mobi.kujon.fragments.UserInfoFragment;

public class StudentDetailsActivity extends BaseActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        String userId = getIntent().getStringExtra(UserInfoFragment.USER_ID);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.placeholder, UserInfoFragment.getFragment(userId));
        transaction.commit();
    }

    public static void showStudentDetails(Activity activity, String userId) {
        Intent intent = new Intent(activity, StudentDetailsActivity.class);
        intent.putExtra(UserInfoFragment.USER_ID, userId);
        activity.startActivity(intent);
    }
}
