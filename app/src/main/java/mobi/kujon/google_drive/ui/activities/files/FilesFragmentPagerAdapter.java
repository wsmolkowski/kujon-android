package mobi.kujon.google_drive.ui.activities.files;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 *
 */

public class FilesFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] strings;
    private Fragment[] fragments;

    public FilesFragmentPagerAdapter(FragmentManager fm, String[] strings, Fragment[] fragments) {
        super(fm);
        this.strings = strings;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return strings[position];
    }
}
