package mobi.kujon.google_drive.ui.activities.files;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import mobi.kujon.google_drive.ui.fragments.files.FilesListFragment;

/**
 *
 */

public class FilesFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] strings;
    private FilesListFragment[] fragments;

    public FilesFragmentPagerAdapter(FragmentManager fm, String[] strings, FilesListFragment[] fragments) {
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

    public void refresh() {
        for(FilesListFragment filesListFragment:fragments){
            filesListFragment.reload();
        }
    }
}
