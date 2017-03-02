package com.tokopedia.discovery.search.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tokopedia.discovery.search.view.fragment.SearchResultFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author erry on 23/02/17.
 */

public class SearchPageAdapter extends FragmentStatePagerAdapter {

    private String[] TITLE = new String[] {
            "Product", "Toko"
    };

    private List<Fragment> fragments = new ArrayList<>();

    public SearchPageAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(SearchResultFragment.newInstance());
        fragments.add(SearchResultFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position];
    }
}