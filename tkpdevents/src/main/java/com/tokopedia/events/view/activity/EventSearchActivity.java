package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
import com.tokopedia.events.view.adapter.EventCategoryAdapter;
import com.tokopedia.events.view.contractor.EventSearchContract;
import com.tokopedia.events.view.customview.SearchInputView;
import com.tokopedia.events.view.presenter.EventSearchPresenter;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

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

    @BindView(R2.id.main_content)
    FrameLayout mainContent;

    @BindView(R2.id.progress_bar_layout)
    View progressBarLayout;
    @BindView(R2.id.prog_bar)
    ProgressBar progBar;
    @BindView(R2.id.search_input_view)
    SearchInputView searchInputView;

    @BindView(R2.id.rv_search_results)
    RecyclerView rvSearchResults;

    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_search);
        unbinder = ButterKnife.bind(this);
        initInjector();
        executeInjector();
        ButterKnife.bind(this);
        searchInputView.setListener(this);
        mPresenter.attachView(this);
        setupToolbar();
        toolbar.setTitle("Events");
        mPresenter.initialize();
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
    public void renderFromSearchResults(List<CategoryItemsViewModel> categoryItemsViewModels) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        EventCategoryAdapter eventCategoryAdapter = new EventCategoryAdapter(getActivity(), categoryItemsViewModels);
        rvSearchResults.setLayoutManager(linearLayoutManager);
        rvSearchResults.setAdapter(eventCategoryAdapter);
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

    @Override
    public FragmentManager getFragmentManagerInstance() {
        return getSupportFragmentManager();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mPresenter.onOptionMenuClick(item.getItemId());
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
