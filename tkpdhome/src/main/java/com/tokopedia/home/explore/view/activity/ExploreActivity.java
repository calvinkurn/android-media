package com.tokopedia.home.explore.view.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.home.R;
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.home.explore.di.DaggerExploreComponent;
import com.tokopedia.home.explore.di.ExploreComponent;
import com.tokopedia.home.explore.domain.model.ExploreDataModel;
import com.tokopedia.home.explore.view.adapter.ExploreFragmentAdapter;
import com.tokopedia.home.explore.view.presentation.ExploreContract;
import com.tokopedia.home.explore.view.presentation.ExplorePresenter;


import javax.inject.Inject;

public class ExploreActivity extends BaseTabActivity implements HasComponent<ExploreComponent>, ExploreContract.View {


    @Inject
    ExplorePresenter presenter;

    private ExploreFragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExploreComponent component = getComponent();
        component.inject(this);
        component.inject(presenter);
        presenter.attachView(this);
        presenter.getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public ExploreComponent getComponent() {
        return DaggerExploreComponent.builder().baseAppComponent(((BaseMainApplication)
                getApplication()).getBaseAppComponent()).build();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showNetworkError(String message) {

    }

    @Override
    public void removeNetworkError() {

    }

    @Override
    public void renderData(ExploreDataModel dataModel) {
        fragmentAdapter.setModelList(dataModel.getDynamicHomeIcon().getLayoutSections());
        for (int i = 0; i < dataModel.getDynamicHomeIcon().getLayoutSections().size(); i++) {
            setupTabIcon(i);
        }
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(ExploreActivity.this, R.color.tab_indicator_beli));
                        break;
                    case 1:
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(ExploreActivity.this, R.color.tab_indicator_bayar));
                        break;
                    case 2:
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(ExploreActivity.this, R.color.tab_indicator_pesan));
                        break;
                    case 3:
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(ExploreActivity.this, R.color.tab_indicator_ajukan));
                        break;
                    case 4:
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(ExploreActivity.this, R.color.tab_indicator_jual));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupTabIcon(int i) {
        View view = LayoutInflater.from(this).inflate(R.layout.explore_tab_item, null, false);
        TextView labelTxt = view.findViewById(R.id.label);
        ImageView iconView = view.findViewById(R.id.icon);
        switch (i) {
            case 0:
                iconView.setImageResource(R.drawable.ic_beli);
                labelTxt.setText(getString(R.string.beli));
                break;
            case 1:
                iconView.setImageResource(R.drawable.ic_bayar);
                labelTxt.setText(getString(R.string.bayar));
                break;
            case 2:
                iconView.setImageResource(R.drawable.ic_pesan);
                labelTxt.setText(getString(R.string.pesan));
                break;
            case 3:
                iconView.setImageResource(R.drawable.ic_ajukan);
                labelTxt.setText(getString(R.string.ajukan));
                break;
            case 4:
                iconView.setImageResource(R.drawable.ic_jual);
                labelTxt.setText(getString(R.string.jual));
                break;
        }
        TabLayout.Tab tab = tabLayout.getTabAt(i);
        tab.setCustomView(view);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_explore;
    }

    @Override
    protected PagerAdapter getViewPagerAdapter() {
        fragmentAdapter = new ExploreFragmentAdapter(getSupportFragmentManager());
        return fragmentAdapter;
    }

    @Override
    protected void setupFragment(Bundle savedinstancestate) {
        super.setupFragment(savedinstancestate);
    }

    @Override
    protected int getPageLimit() {
        return 3;
    }


}
