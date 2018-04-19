package com.tokopedia.tokocash.autosweepmf.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tokopedia.tokocash.autosweepmf.view.fragment.HelpWebViewFragment;

import java.util.List;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class HelpPagerAdapter extends FragmentPagerAdapter {

    private List<String> mItems;

    public HelpPagerAdapter(FragmentManager fm, List<String> items) {
        super(fm);
        this.mItems = items;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a HelpWebViewFragment (defined as a static inner class below).
        return HelpWebViewFragment.newInstance(mItems.get(position));
    }

    @Override
    public int getCount() {
        return mItems.size();
    }
}