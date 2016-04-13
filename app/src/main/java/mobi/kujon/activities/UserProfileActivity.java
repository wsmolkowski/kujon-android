package mobi.kujon.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import mobi.kujon.R;
import mobi.kujon.fragments.PlanFragment;
import mobi.kujon.fragments.UserInfoFragment;
import mobi.kujon.ui.CustomViewPager;

public class UserProfileActivity extends BaseActivity {

    @Bind(R.id.viewPager) CustomViewPager pager;
    private PagerAdapter adapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        adapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                System.out.println("" + tab.getPosition());
                pager.setCurrentItem(tab.getPosition());
            }

            @Override public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            @Override public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        };

        for (int i = 0; i < adapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(adapter.TITLES[i])
                            .setTabListener(tabListener));
        }

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                getSupportActionBar().setSelectedNavigationItem(position);
                pager.setPagingEnabled(position != 1);
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public String[] TITLES = new String[]{"Użytkownik", "Plan", "Przedmioty", "Oceny", "Nauczyciele", "Uczelnia"};
        public Fragment[] FRAGMENTS = new Fragment[]{new UserInfoFragment(), new PlanFragment(), new UserInfoFragment(), new UserInfoFragment(), new UserInfoFragment(), new UserInfoFragment()};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FRAGMENTS[position];
        }

        @Override
        public int getCount() {
            return 6;
        }
    }
}
