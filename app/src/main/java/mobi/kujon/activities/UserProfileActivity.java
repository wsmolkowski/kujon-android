package mobi.kujon.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

import butterknife.Bind;
import butterknife.ButterKnife;
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

        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withCloseOnClick(true)
                .build();

        for (String title : TITLES) {
            drawer.addItem(new PrimaryDrawerItem().withName(title));
        }

        drawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.placeholder, FRAGMENTS[position]);
            fragmentTransaction.commit();
            drawer.closeDrawer();
            toolbar.setTitle(TITLES[position]);
            return true;
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        handler.post(() -> drawer.setSelectionAtPosition(0));
    }
}
