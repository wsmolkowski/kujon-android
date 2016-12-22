package mobi.kujon.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.KujonApplication;
import mobi.kujon.R;
import mobi.kujon.fragments.CoursesFragment;
import mobi.kujon.fragments.GradesFragment;
import mobi.kujon.fragments.LecturersFragment;
import mobi.kujon.fragments.MessagesFragment;
import mobi.kujon.fragments.PlanFragment;
import mobi.kujon.fragments.PlanListFragment;
import mobi.kujon.fragments.SearchFragment;
import mobi.kujon.fragments.UserInfoFragment;
import mobi.kujon.utils.ErrorHandlerUtil;

public class MainActivity extends BaseActivity {

    public static final int CALENDAR_POSITION = 1;
    @Bind(R.id.toolbar) Toolbar toolbar;

    public String[] TITLES = new String[]{"Użytkownik", "Plan zajęć", "Przedmioty", "Oceny", "Wykładowcy", "Wiadomości","Szukaj"};
    public int[] ICONS = new int[]{R.drawable.user, R.drawable.plan, R.drawable.courses, R.drawable.grades, R.drawable.teachers, R.drawable.ic_messages, R.drawable.search};
    public Fragment[] FRAGMENTS = new Fragment[]{
            new UserInfoFragment(), new PlanFragment(), new CoursesFragment(), new GradesFragment(), new LecturersFragment(), new MessagesFragment(), new SearchFragment()};
    @Bind(R.id.toolbar_title) TextView toolbarTitle;
    private Drawer drawer;
    private AccountHeader headerResult;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        KujonApplication.getApplication().getLoginStatus().onSuccessTask(task -> {
            GoogleSignInResult loginStatus = task.getResult();
            String displayName = loginStatus.getSignInAccount().getDisplayName();
            String email = loginStatus.getSignInAccount().getEmail();
            Uri photoUrl = loginStatus.getSignInAccount().getPhotoUrl();
            ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem()
                    .withName(displayName)
                    .withEmail(email)
                    .withIcon(photoUrl);

            headerResult.addProfiles(profileDrawerItem);
            return null;
        }, Task.UI_THREAD_EXECUTOR).continueWith(ErrorHandlerUtil.ERROR_HANDLER);


        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimaryDark)
//                .withHeaderBackground(new ImageHolder(Uri.parse("https://kujon.mobi/static/img/logo/logo-demo-64x64.jpg")))
//                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER)
//                .addProfiles(profileDrawerItem)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withCloseOnClick(true)
                .withAccountHeader(headerResult)
                .build();

        Typeface latoSemiBold = Typeface.createFromAsset(getAssets(), "fonts/Lato-Semibold.ttf");

        for (int i = 0; i < FRAGMENTS.length; i++) {
            final int finalI = i;
            drawer.addItem(new PrimaryDrawerItem()
                    .withName(TITLES[i])
                    .withIcon(ICONS[i])
                    .withTypeface(latoSemiBold)
                    .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                        showFragment(FRAGMENTS[finalI], true);
                        return true;
                    }));
        }

        drawer.addItem(new DividerDrawerItem());
        drawer.addItem(new PrimaryDrawerItem()
                .withName(R.string.settings)
                .withSelectable(false)
                .withIcon(R.drawable.settings)
                .withTypeface(latoSemiBold)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    drawer.closeDrawer();
                    startActivity(new Intent(this, PreferencesActivity.class));
                    return true;
                }));

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        showFragment(FRAGMENTS[0], false);
    }

    private void showFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeholder, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
        this.drawer.closeDrawer();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_to_calendar:
                FRAGMENTS[CALENDAR_POSITION] = new PlanFragment();
                showFragment(FRAGMENTS[CALENDAR_POSITION], true);
                return true;
            case R.id.switch_to_list:
                FRAGMENTS[CALENDAR_POSITION] = new PlanListFragment();
                showFragment(FRAGMENTS[CALENDAR_POSITION], true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setToolbarTitle(int title) {
        toolbarTitle.setText(title);
    }

}
