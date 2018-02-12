package com.tokopedia.home.explore.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.tokopedia.home.explore.view.adapter.viewmodel.ExploreSectionViewModel;
import com.tokopedia.home.explore.view.fragment.ExploreFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class ExploreFragmentAdapter extends FragmentPagerAdapter {

    private List<ExploreSectionViewModel> modelList;
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public ExploreFragmentAdapter(FragmentManager fm) {
        super(fm);
        modelList = new ArrayList<>();
    }

    public void setData(List<ExploreSectionViewModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        ExploreFragment fragment = ExploreFragment.newInstance(position);
        fragment.setData(modelList.get(position));
        return fragment;
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

    public ExploreFragment getRegisteredFragment(int position) {
        return (ExploreFragment) registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

}
