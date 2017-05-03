package com.tokopedia.discovery.search.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.tokopedia.discovery.R;
import com.tokopedia.discovery.search.view.fragment.SearchResultFragment;

/**
 * @author erry on 23/02/17.
 */

public class SearchPageAdapter extends FragmentStatePagerAdapter {

    private String[] TITLE;
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public SearchPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        TITLE = new String[]{
                context.getString(R.string.title_product),
                context.getString(R.string.title_shop)
        };
    }

    @Override
    public Fragment getItem(int position) {
        return SearchResultFragment.newInstance();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public SearchResultFragment getRegisteredFragment(int position) {
        return (SearchResultFragment) registeredFragments.get(position);
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