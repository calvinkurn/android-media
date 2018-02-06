package com.tokopedia.home.explore.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.category.CategoryLayoutSectionsModel;
import com.tokopedia.home.explore.domain.model.LayoutSections;
import com.tokopedia.home.explore.view.adapter.viewmodel.CategoryGridListViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.ExploreSectionViewModel;
import com.tokopedia.home.explore.view.adapter.viewmodel.SellViewModel;
import com.tokopedia.home.explore.view.fragment.ExploreFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class ExploreFragmentAdapter extends FragmentStatePagerAdapter {

    private List<ExploreSectionViewModel> modelList;

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
        ExploreFragment fragment = ExploreFragment.newInstance();
        fragment.setData(modelList.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

}
