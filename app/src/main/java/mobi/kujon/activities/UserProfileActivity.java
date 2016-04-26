package mobi.kujon.activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.fragments.CoursesFragment;
import mobi.kujon.fragments.GradesFragment;
import mobi.kujon.fragments.LecturersFragment;
import mobi.kujon.fragments.PlanFragment;
import mobi.kujon.fragments.ProgrammesFragment;
import mobi.kujon.fragments.TermsFragment;
import mobi.kujon.fragments.UserInfoFragment;

public class UserProfileActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    Handler handler = new Handler();

    public String[] TITLES = new String[]{"Użytkownik", "Plan", "Przedmioty", "Oceny", "Nauczyciele", "Kierunki", "Cykle"};
    public Fragment[] FRAGMENTS = new Fragment[]{
            new UserInfoFragment(), new PlanFragment(), new CoursesFragment(), new GradesFragment(), new LecturersFragment(), new ProgrammesFragment(), new TermsFragment()};

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        GoogleSignInResult loginStatus = KujonApplication.getApplication().getLoginStatus();

        String displayName = loginStatus.getSignInAccount().getDisplayName();
        String email = loginStatus.getSignInAccount().getEmail();
        Uri photoUrl = loginStatus.getSignInAccount().getPhotoUrl();
        ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem().withName(displayName).withEmail(email).withIcon(photoUrl);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimaryDark)
                .addProfiles(profileDrawerItem)
                .build();

        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withCloseOnClick(true)
                .withAccountHeader(headerResult)
                .build();

        for (String title : TITLES) {
            drawer.addItem(new PrimaryDrawerItem().withName(title));
        }

        drawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.placeholder, FRAGMENTS[position - 1]);
            fragmentTransaction.commit();
            drawer.closeDrawer();
            toolbar.setTitle(TITLES[position - 1]);
            return true;
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        handler.post(() -> drawer.setSelectionAtPosition(1));
    }
}
