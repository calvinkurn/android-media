package com.tokopedia.discovery.search.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.autocomplete.DefaultAutoCompleteViewModel;
import com.tokopedia.discovery.autocomplete.HostAutoCompleteAdapter;
import com.tokopedia.discovery.autocomplete.HostAutoCompleteFactory;
import com.tokopedia.discovery.autocomplete.HostAutoCompleteTypeFactory;
import com.tokopedia.discovery.autocomplete.TabAutoCompleteViewModel;
import com.tokopedia.discovery.autocomplete.di.AutoCompleteComponent;
import com.tokopedia.discovery.autocomplete.di.DaggerAutoCompleteComponent;
import com.tokopedia.discovery.catalog.analytics.AppScreen;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.discovery.search.SearchPresenter;
import com.tokopedia.discovery.search.view.SearchContract;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;

import javax.inject.Inject;

/**
 * @author erry on 23/02/17.
 */

public class SearchMainFragment extends TkpdBaseV4Fragment implements SearchContract.View, ItemClickListener {
    public static final int PAGER_POSITION_PRODUCT = 0;
    public static final int PAGER_POSITION_SHOP = 1;


    public static final String FRAGMENT_TAG = "SearchHistoryFragment";
    public static final String INIT_QUERY = "INIT_QUERY";
    private static final String SEARCH_PARAMETER = "SEARCH_PARAMETER";
    private static final String MP_SEARCH_AUTOCOMPLETE = "mp_search_autocomplete";

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    @Inject
    SearchPresenter presenter;

    private HostAutoCompleteAdapter adapter;
    private String networkErrorMessage;
    private boolean onTabShop;

    private SearchParameter searchParameter;
    private PerformanceMonitoring performanceMonitoring;

    public static SearchMainFragment newInstance() {

        Bundle args = new Bundle();

        SearchMainFragment fragment = new SearchMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SearchMainFragment newInstance(String query) {

        Bundle args = new Bundle();
        args.putString(INIT_QUERY, query);
        SearchMainFragment fragment = new SearchMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initResources();
        AutoCompleteComponent component = DaggerAutoCompleteComponent.builder()
                .baseAppComponent(((MainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        component.inject(this);
        component.inject(presenter);
        setRetainInstance(true);
    }

    private void initResources() {
        networkErrorMessage = getString(R.string.msg_network_error);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_universearch, container, false);
        recyclerView = parentView.findViewById(R.id.list);
        prepareView();
        presenter.attachView(this);
        presenter.initializeDataSearch();
        return parentView;
    }

    private void prepareView() {
        HostAutoCompleteTypeFactory typeFactory = new HostAutoCompleteFactory(
                this,
                getChildFragmentManager()
        );
        layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
        adapter = new HostAutoCompleteAdapter(typeFactory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.scrollToPosition(0);
        recyclerView.setNestedScrollingEnabled(false);
    }

    public void setCurrentTab(final int pos) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public int getCurrentTab() {
        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 1) {
            return isOnTabShop() ? SearchMainFragment.PAGER_POSITION_SHOP : SearchMainFragment.PAGER_POSITION_PRODUCT;
        } else {
            return SearchMainFragment.PAGER_POSITION_PRODUCT;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_UNIVERSEARCH;
    }

    @Override
    public void showAutoCompleteResult(DefaultAutoCompleteViewModel defaultAutoCompleteViewModel,
                                       TabAutoCompleteViewModel tabAutoCompleteViewModel) {
        stopTracePerformanceMonitoring();
        adapter.setDefaultViewModel(defaultAutoCompleteViewModel);
        adapter.setSuggestionViewModel(tabAutoCompleteViewModel);
        if (defaultAutoCompleteViewModel.getList().isEmpty()) {
            recyclerView.scrollToPosition(1);
        } else {
            recyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void showNetworkErrorMessage() {
        SnackbarManager.make(getActivity(), networkErrorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null) {
            searchParameter = savedInstanceState.getParcelable(SEARCH_PARAMETER);
            if(searchParameter != null) {
                presenter.search(searchParameter);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SEARCH_PARAMETER, searchParameter);
    }

    public void search(SearchParameter searchParameter){
        performanceMonitoring = PerformanceMonitoring.start(MP_SEARCH_AUTOCOMPLETE);
        presenter.search(searchParameter);
    }

    public void deleteAllRecentSearch(){
        presenter.deleteAllRecentSearch();
    }

    public void deleteRecentSearch(String keyword){
        presenter.deleteRecentSearchItem(keyword);
    }

    private void dropKeyBoard() {
        if (getActivity() != null && getActivity() instanceof DiscoveryActivity) {
            ((DiscoveryActivity) getActivity()).dropKeyboard();
        }
    }

    @Override
    public void onItemClicked(String applink, String webUrl) {
        dropKeyBoard();

        Intent intent = getIntentForItemClicked(applink, webUrl);

        if(isActivityCalledForResult()) {
            setAutoCompleteActivityResult(intent);
        }
        else {
            startActivityFromAutoComplete(intent);
        }
    }

    private Intent getIntentForItemClicked(String applink, String webUrl) {
        if(getActivity() == null || getActivity().getApplicationContext() == null) return null;

        Intent intent;

        if(isActivityAnApplinkRouter()) {
            intent = createIntentForApplinkIfSupported(applink, webUrl);
        }
        else {
            intent = createIntentForWebView(webUrl);
        }

        return intent;
    }

    private boolean isActivityAnApplinkRouter() {
        return getActivity() != null && getActivity().getApplicationContext() instanceof ApplinkRouter;
    }

    private Intent createIntentForApplinkIfSupported(String applink, String webUrl) {
        Intent intent;

        ApplinkRouter router = ((ApplinkRouter) getActivity().getApplicationContext());

        if (router.isSupportApplink(applink)) {
            intent = RouteManager.getIntent(getActivity(), applink);
        } else {
            intent = createIntentForWebView(webUrl);
        }

        return intent;
    }

    private Intent createIntentForWebView(String webUrl) {
        if (!TextUtils.isEmpty(webUrl)) {
            Intent intent = new Intent(getActivity(), BannerWebView.class);
            intent.putExtra("url", webUrl);
        }

        return null;
    }

    private boolean isActivityCalledForResult() {
        return getActivity() != null
                && getActivity().getCallingActivity() != null;
    }

    private void setAutoCompleteActivityResult(Intent intent) {
        if(intent == null || getActivity() == null) return;

        getActivity().setResult(SearchConstant.AUTO_COMPLETE_ACTIVITY_RESULT_CODE_START_ACTIVITY, intent);
        getActivity().finish();
    }

    private void startActivityFromAutoComplete(Intent intent) {
        if(intent == null || getActivity() == null) return;

        getActivity().finish();
        startActivity(intent);
    }

    @Override
    public void copyTextToSearchView(String text) {
        ((DiscoveryActivity) getActivity()).setSearchQuery(text + " ");
    }

    @Override
    public void onDeleteRecentSearchItem(String keyword) {
        ((DiscoveryActivity) getActivity()).deleteRecentSearch(keyword);
    }

    @Override
    public void onDeleteAllRecentSearch() {
        ((DiscoveryActivity) getActivity()).deleteAllRecentSearch();
    }

    @Override
    public void setOnTabShop(boolean onTabShop) {
        this.onTabShop = onTabShop;
    }

    public boolean isOnTabShop() {
        return onTabShop;
    }

    public void setSearchParameter(SearchParameter searchParameter) {
        this.searchParameter = searchParameter;
    }

    private void stopTracePerformanceMonitoring() {
        if (performanceMonitoring != null) {
            performanceMonitoring.stopTrace();
        }
    }
}
