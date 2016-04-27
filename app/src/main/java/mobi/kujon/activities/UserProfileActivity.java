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
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
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

        for (int i = 0; i < TITLES.length; i++) {
            final int finalI = i;
            drawer.addItem(new PrimaryDrawerItem().withName(TITLES[i]).withOnDrawerItemClickListener((view, position, drawerItem) -> {
                showFragment(drawer, FRAGMENTS[finalI], TITLES[finalI]);
                return true;
            }));
        }

        drawer.addItem(new DividerDrawerItem());
        drawer.addItem(
                new PrimaryDrawerItem()
                        .withName("Wyloguj")
                        .withSelectable(false)
                        .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                            logout();
                            return true;
                        }));
        drawer.addItem(new PrimaryDrawerItem().withName("Skasuj").withOnDrawerItemClickListener((view, position, drawerItem) -> {
            deleteAccount();
            return true;
        }));
        drawer.addItem(new PrimaryDrawerItem().withName("Prześlij opinię").withOnDrawerItemClickListener((view, position, drawerItem) -> {
            contactUs();
            return true;
        }));

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        showFragment(drawer, FRAGMENTS[0], TITLES[0]);
    }

    private void showFragment(Drawer drawer, Fragment fragment, String title) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, fragment);
        fragmentTransaction.commit();
        drawer.closeDrawer();
        toolbar.setTitle(title);
    }
}
