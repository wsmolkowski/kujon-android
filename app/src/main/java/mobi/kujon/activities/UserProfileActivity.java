package mobi.kujon.activities;

import android.content.Intent;
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
    public int[] ICONS = new int[]{R.drawable.user, R.drawable.plan, R.drawable.courses, R.drawable.grades, R.drawable.teachers, R.drawable.terms, R.drawable.programmes};
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
        ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem()
                .withName(displayName)
                .withEmail(email)
                .withIcon(photoUrl);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimaryDark)
//                .withHeaderBackground(new ImageHolder(Uri.parse("https://kujon.mobi/static/img/logo/logo-demo-64x64.jpg")))
//                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER)
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
            drawer.addItem(new PrimaryDrawerItem()
                    .withName(TITLES[i])
                    .withIcon(ICONS[i])
                    .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                        showFragment(drawer, FRAGMENTS[finalI], TITLES[finalI]);
                        return true;
                    }));
        }

        drawer.addItem(new DividerDrawerItem());
        drawer.addItem(new PrimaryDrawerItem()
                .withName("Wyloguj")
                .withSelectable(false)
                .withIcon(R.drawable.logout)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    logout();
                    drawer.closeDrawer();
                    return true;
                }));
        drawer.addItem(new PrimaryDrawerItem()
                .withName("Skasuj")
                .withSelectable(false)
                .withIcon(R.drawable.remove_account)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    deleteAccount();
                    drawer.closeDrawer();
                    return true;
                }));
        drawer.addItem(new PrimaryDrawerItem()
                .withName("Prześlij opinię")
                .withSelectable(false)
                .withIcon(R.drawable.contact_us)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    contactUs();
                    drawer.closeDrawer();
                    return true;
                }));

        drawer.addItem(new PrimaryDrawerItem()
                .withName(R.string.share_app)
                .withSelectable(false)
                .withIcon(R.drawable.share)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.app_link));
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    drawer.closeDrawer();
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
