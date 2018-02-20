package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.events.view.adapter.EventCategoryAdapter;
import com.tokopedia.events.view.adapter.TopEventsSuggestionsAdapter;
import com.tokopedia.events.view.contractor.EventSearchContract;
import com.tokopedia.events.view.customview.SearchInputView;
import com.tokopedia.events.view.presenter.EventSearchPresenter;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.SearchViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class EventSearchActivity extends TActivity implements
        EventSearchContract.IEventSearchView, SearchInputView.Listener {

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
    @BindView(R2.id.rv_top_events_suggestions)
    RecyclerView rvTopEventSuggestions;
    @BindView(R2.id.tv_topevents)
    TextView tvTopevents;

    LinearLayoutManager layoutManager;

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
        toolbar.setTitle("Events");
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mPresenter.initialize();

    }

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, EventSearchActivity.class);
    }

    @Override
    public void onSearchSubmitted(String text) {
        mPresenter.searchSubmitted(text);
    }

    @Override
    public void onSearchTextChanged(String text) {

        mPresenter.searchTextChanged(text);

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
        if (categoryItemsViewModels != null && categoryItemsViewModels.size() != 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            EventCategoryAdapter eventCategoryAdapter = new EventCategoryAdapter(getActivity(), categoryItemsViewModels);
            rvSearchResults.setLayoutManager(linearLayoutManager);
            rvSearchResults.setAdapter(eventCategoryAdapter);
            tvTopevents.setVisibility(View.GONE);
            rvTopEventSuggestions.setVisibility(View.GONE);
        } else {
            rvSearchResults.setVisibility(View.GONE);
            rvTopEventSuggestions.setVisibility(View.GONE);
            tvTopevents.setText("No Events Found");
            tvTopevents.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void setTopEvents(List<SearchViewModel> searchViewModels) {
        if (searchViewModels != null && !searchViewModels.isEmpty()) {
            TopEventsSuggestionsAdapter adapter = new TopEventsSuggestionsAdapter(this, searchViewModels, mPresenter);
            rvTopEventSuggestions.setLayoutManager(layoutManager);
            rvTopEventSuggestions.setAdapter(adapter);
            rvTopEventSuggestions.removeOnScrollListener(rvOnScrollListener);
            tvTopevents.setText("TOP EVENTS");
            tvTopevents.setVisibility(View.VISIBLE);
            rvTopEventSuggestions.setVisibility(View.VISIBLE);
            rvSearchResults.setVisibility(View.GONE);
        } else {
            tvTopevents.setVisibility(View.GONE);
            rvTopEventSuggestions.setVisibility(View.GONE);
            rvSearchResults.setVisibility(View.GONE);
        }
    }

    @Override
    public void setSuggestions(List<SearchViewModel> suggestions, String highlight) {
        if (suggestions != null && !suggestions.isEmpty()) {
            TopEventsSuggestionsAdapter adapter = new TopEventsSuggestionsAdapter(this, suggestions, mPresenter);
            adapter.setHighLightText(highlight);
            rvTopEventSuggestions.setLayoutManager(layoutManager);
            rvTopEventSuggestions.setAdapter(adapter);
            rvTopEventSuggestions.addOnScrollListener(rvOnScrollListener);
            tvTopevents.setVisibility(View.GONE);
            rvTopEventSuggestions.setVisibility(View.VISIBLE);
            rvSearchResults.setVisibility(View.GONE);
        } else {
            rvSearchResults.setVisibility(View.GONE);
            rvTopEventSuggestions.setVisibility(View.GONE);
            tvTopevents.setText("No Events Found");
            tvTopevents.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void removeFooter() {
        ((TopEventsSuggestionsAdapter) rvTopEventSuggestions.getAdapter()).removeFooter();
    }

    @Override
    public void addFooter() {
        ((TopEventsSuggestionsAdapter) rvTopEventSuggestions.getAdapter()).addFooter();

    }

    @Override
    public void addEvents(List<SearchViewModel> searchViewModels) {
        ((TopEventsSuggestionsAdapter) rvTopEventSuggestions.getAdapter()).addAll(searchViewModels);

    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
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
    protected boolean isLightToolbarThemes() {
        return true;
    }

    private RecyclerView.OnScrollListener rvOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mPresenter.onRecyclerViewScrolled(layoutManager);
        }
    };
}
