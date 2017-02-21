package mobi.kujon.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.view.BezelImageView;

import javax.inject.Inject;

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
import mobi.kujon.google_drive.ui.activities.courses.CoursesInSemseterActivity;
import mobi.kujon.network.ApiProvider;
import mobi.kujon.network.ApiType;
import mobi.kujon.utils.ErrorHandlerUtil;

import static mobi.kujon.KujonApplication.FROM_NOTIFICATION;

public class MainActivity extends BaseActivity {

    @Inject
    ApiProvider apiProvider;

    public static final int CALENDAR_POSITION = 1;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    public int[] TITLES = new int[]{R.string.user, R.string.plan, R.string.courses, R.string.grades, R.string.teachers, R.string.messages, R.string.search};
    public int[] ICONS = new int[]{R.drawable.user, R.drawable.plan, R.drawable.courses, R.drawable.grades, R.drawable.teachers, R.drawable.ic_messages, R.drawable.search};
    public Fragment[] FRAGMENTS = new Fragment[]{
            new UserInfoFragment(), new PlanFragment(), new CoursesFragment(), new GradesFragment(), new LecturersFragment(), new MessagesFragment(), new SearchFragment()};
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    private Drawer drawer;
    private AccountHeader headerResult;
    private int headerBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        KujonApplication.getComponent().inject(this);
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
            updateHeaderIfFailed(displayName, email, photoUrl);
            return null;
        }, Task.UI_THREAD_EXECUTOR).continueWith(ErrorHandlerUtil.ERROR_HANDLER);

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(getHeaderBackground())
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
                        if (position < 7) {
                            showFragment(FRAGMENTS[finalI], true);
                        } else {
                            showFragment(FRAGMENTS[finalI - 1], true);
                        }
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
        showFragment(selectFragmentToShow(), false);
    }

    private void updateHeaderIfFailed(String displayName, String email, Uri photoUrl) {
        if (headerResult == null || headerResult.getActiveProfile() != null) {
            return;
        }
        View headerView = headerResult.getView();
        if (headerView == null) {
            Log.d("MaterialDrawer", "Header View not visible");
            return;
        }
        setHeaderImage(photoUrl, headerView);
        setHeaderText(displayName, headerView, R.id.material_drawer_account_header_name);
        setHeaderText(email, headerView, R.id.material_drawer_account_header_email);
    }

    private void setHeaderImage(Uri photoUrl, View headerView) {
        BezelImageView headerImageView = (BezelImageView) headerView.findViewById(R.id.material_drawer_account_header_current);
        if (headerImageView != null) {
            headerImageView.setVisibility(View.VISIBLE);
            headerImageView.setImageURI(photoUrl);
        }
    }

    private void setHeaderText(String text, View headerView, int textViewId) {
        TextView textView = (TextView) headerView.findViewById(textViewId);
        if (textView != null) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }

    private Fragment selectFragmentToShow() {
        return getIntent().getBooleanExtra(FROM_NOTIFICATION, false) ? FRAGMENTS[5] : FRAGMENTS[0];
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    private int getHeaderBackground() {
        switch (apiProvider.getApiType()) {
            case ApiType.DEMO:
                return R.drawable.demo;
            case ApiType.PROD:
                return R.color.primary;
        }
        return R.color.primary;
    }
}
