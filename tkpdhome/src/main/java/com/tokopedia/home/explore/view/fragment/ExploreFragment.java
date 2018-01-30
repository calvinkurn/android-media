package com.tokopedia.home.explore.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.home.R;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.home.explore.view.adapter.ExploreAdapter;
import com.tokopedia.home.explore.view.adapter.TypeFactory;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class ExploreFragment extends BaseListFragment<Visitable<TypeFactory>, TypeFactory> {

    public static ExploreFragment newInstance() {

        Bundle args = new Bundle();

        ExploreFragment fragment = new ExploreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onItemClicked(Visitable<TypeFactory> visitable) {

    }

    @Override
    public void loadData(int page) {

    }

    @Override
    protected TypeFactory getAdapterTypeFactory() {
        return new ExploreAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }
}
