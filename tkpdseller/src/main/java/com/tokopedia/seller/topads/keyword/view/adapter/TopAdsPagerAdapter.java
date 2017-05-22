package com.tokopedia.seller.topads.keyword.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordListFragment;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordNegativeListFragment;

/**
 * @author normansyahputa on 5/19/17.
 */

public class TopAdsPagerAdapter extends FragmentStatePagerAdapter {

    public static final int POSITIF = 0;
    public static final int NEGATIVE = 1;
    SparseArrayCompat<Fragment> registeredFragments = new SparseArrayCompat<Fragment>();
    private String[] title;

    public TopAdsPagerAdapter(FragmentManager fm, String[] title) {
        super(fm);
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case POSITIF:
                return TopAdsKeywordListFragment.createInstance();
            default:
            case NEGATIVE:
                return TopAdsKeywordNegativeListFragment.createInstance();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case POSITIF:
                return title[POSITIF];
            default:
            case NEGATIVE:
                return title[NEGATIVE];
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object o = super.instantiateItem(container, position);
        registeredFragments.put(position, (Fragment) o);
        return o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
