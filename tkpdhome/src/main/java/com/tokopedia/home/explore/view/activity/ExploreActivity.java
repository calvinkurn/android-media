package com.tokopedia.home.explore.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.core.analytics.HomePageTracking;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.home.R;
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon;
import com.tokopedia.home.explore.di.DaggerExploreComponent;
import com.tokopedia.home.explore.di.ExploreComponent;
import com.tokopedia.home.explore.view.adapter.ExploreFragmentAdapter;
import com.tokopedia.home.explore.view.adapter.viewmodel.ExploreSectionViewModel;
import com.tokopedia.home.explore.view.fragment.ExploreFragment;
import com.tokopedia.home.explore.view.presentation.ExploreContract;
import com.tokopedia.home.explore.view.presentation.ExplorePresenter;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ExploreActivity extends BaseTabActivity implements HasComponent<ExploreComponent>, ExploreContract.View {

    private static final String SECTION = "section";
    private static final String POSTION = "position";
    private static final String DEFAULT_SECTION = "beli";

    public static final int REQUEST_LOGIN = 384;
    private static final String TAG = ExploreFragment.class.getSimpleName();
    @Inject
    ExplorePresenter presenter;

    private ExploreFragmentAdapter fragmentAdapter;
    private SnackbarRetry messageSnackbar;
    private CoordinatorLayout root;
    private int position = 0;
    private List<String> titleList = new ArrayList<>();

    @DeepLink(Constants.Applinks.EXPLORE)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ExploreActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(POSTION);
        }
        ExploreComponent component = getComponent();
        component.inject(this);
        component.inject(presenter);
        presenter.attachView(this);
        presenter.getData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSTION, viewPager.getCurrentItem());
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
        if (fragmentAdapter.getCount() > 0) {
            if (messageSnackbar == null) {
                messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(this,
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                presenter.getData();
                            }
                        });
            }
            messageSnackbar.showRetrySnackbar();
        } else {
            NetworkErrorHelper.showEmptyState(this, root, message,
                    new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {

                            presenter.getData();
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_LOGIN){
            presenter.getData();
        }
    }

    @Override
    public void removeNetworkError() {

    }

    @Override
    public void renderData(List<ExploreSectionViewModel> list) {
        fragmentAdapter.setData(list);
        setupTabIcon(list);
        if (getIntent().getExtras() != null) {
            String section = getIntent().getStringExtra(SECTION);
            position = sectionToPosition(section != null ? section : DEFAULT_SECTION);
        }
        viewPager.setCurrentItem(position);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        root = findViewById(R.id.root);
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

    private int sectionToPosition(String section) {
        return titleList.indexOf(section.toLowerCase());
    }


    private void setupTabIcon(List<ExploreSectionViewModel> list) {
        titleList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DynamicHomeIcon.UseCaseIcon model = list.get(i).getUseCaseIcon();
            View view = LayoutInflater.from(this).inflate(R.layout.explore_tab_item, null, false);
            TextView labelTxt = view.findViewById(R.id.label);
            ImageView iconView = view.findViewById(R.id.icon);
            String title = model.getName();
            Glide.with(this).load(model.getImageUrl()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(iconView);
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            labelTxt.setText(title);
            view.setOnClickListener(new OnTabExplorerClickListener(title, tabLayout, i));
            tab.setCustomView(view);
            titleList.add(title.toLowerCase());
        }
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

    private class OnTabExplorerClickListener implements View.OnClickListener {
        private final String title;
        private final TabLayout tab;
        private final int positionTab;

        OnTabExplorerClickListener(String title, TabLayout tab, int positionTab) {
            this.title = title;
            this.tab = tab;
            this.positionTab = positionTab;
        }

        @Override
        public void onClick(View v) {
            TabLayout.Tab currentTab = tab.getTabAt(positionTab);
            if (positionTab != tab.getSelectedTabPosition() && currentTab != null) {
                HomePageTracking.eventClickTabExplorer(title);
                currentTab.select();
            }
        }
    }
}
