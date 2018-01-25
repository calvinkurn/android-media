package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.events.view.adapter.CategoryFragmentPagerAdapter;
import com.tokopedia.events.view.contractor.EventSearchContract;
import com.tokopedia.events.view.customview.EventCategoryView;
import com.tokopedia.events.view.customview.SearchInputView;
import com.tokopedia.events.view.presenter.EventSearchPresenter;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class EventSearchActivity extends TActivity implements
        EventSearchContract.EventSearchView, SearchInputView.Listener {

    EventComponent eventComponent;
    @Inject
    public EventSearchPresenter mPresenter;

    @BindView(R2.id.category_view_pager)
    ViewPager categoryViewPager;
    @BindView(R2.id.tabs)
    TabLayout tabs;
    @BindView(R2.id.main_content)
    FrameLayout mainContent;

    @BindView(R2.id.progress_bar_layout)
    View progressBarLayout;
    @BindView(R2.id.prog_bar)
    ProgressBar progBar;
    @BindView(R2.id.search_input_view)
    SearchInputView searchInputView;

    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_search);
        unbinder = ButterKnife.bind(this);
        initInjector();
        executeInjector();
        mPresenter.attachView(this);
        ButterKnife.bind(this);
        setupToolbar();
        searchInputView.setListener(this);
        toolbar.setTitle("Events");
    }

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, EventSearchActivity.class);
    }

    @Override
    public void onSearchSubmitted(String text) {
        mPresenter.getEventsListBySearch(text);
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void renderFromSearchResults(List<CategoryViewModel> categoryViewModels) {
        ArrayList<EventCategoryView> eventCategoryViews = new ArrayList<>();
        for (CategoryViewModel categoryViewModel : categoryViewModels) {
            if (categoryViewModel.getItems() == null || categoryViewModel.getItems().size() == 0) {
                continue;
            }
        }

        CategoryFragmentPagerAdapter categoryTabsPagerAdapter =
                new CategoryFragmentPagerAdapter(getSupportFragmentManager(), categoryViewModels);
        categoryViewPager.setAdapter(categoryTabsPagerAdapter);
        tabs.setupWithViewPager(categoryViewPager);
        categoryViewPager.setCurrentItem(0);
        categoryViewPager.setSaveFromParentEnabled(false);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public RequestParams getParams() {
        return null;
    }

    @Override
    public View getRootView() {
        return null;
    }

    private void executeInjector() {
        if (eventComponent == null) initInjector();
        eventComponent.inject(this);
    }

    private void initInjector() {
        eventComponent = DaggerEventComponent.builder()
                .appComponent(getApplicationComponent())
                .eventModule(new EventModule(this))
                .build();
    }
}
