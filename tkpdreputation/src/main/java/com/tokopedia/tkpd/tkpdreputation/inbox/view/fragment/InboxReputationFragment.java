package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationDetailActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFilterActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.InboxReputationAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox.InboxReputationTypeFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox.InboxReputationTypeFactoryImpl;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter.InboxReputationPresenter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.ReputationDataViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailPassModel;

import javax.inject.Inject;


/**
 * @author by nisie on 8/11/17.
 */

public class InboxReputationFragment extends BaseDaggerFragment
        implements InboxReputation.View {

    public final static String PARAM_TAB = "tab";
    private static final int REQUEST_OPEN_DETAIL = 101;
    private static final int REQUEST_FILTER = 102;
    private static final String ARGS_TIME_FILTER = "ARGS_TIME_FILTER";
    private static final String ARGS_SCORE_FILTER = "ARGS_SCORE_FILTER";
    private static final String ARGS_QUERY = "ARGS_QUERY";

    SearchView searchView;
    private RecyclerView mainList;
    private SwipeToRefresh swipeToRefresh;
    private LinearLayoutManager layoutManager;
    private InboxReputationAdapter adapter;
    private String timeFilter;
    private String scoreFilter;
    private View filterButton;

    @Inject
    InboxReputationPresenter presenter;

    @Inject
    GlobalCacheManager cacheManager;

    @Inject
    SessionHandler sessionHandler;

    public static Fragment createInstance(int tab) {
        InboxReputationFragment fragment = new InboxReputationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_TAB, tab);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_INBOX_REPUTATION;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);

        DaggerReputationComponent reputationComponent =
                (DaggerReputationComponent) DaggerReputationComponent
                        .builder()
                        .appComponent(appComponent)
                        .build();

        reputationComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initVar(savedInstanceState);
    }

    private void openFilter() {

        Intent intent = InboxReputationFilterActivity.createIntent(getActivity(),
                timeFilter, scoreFilter, getTab());
        startActivityForResult(intent, REQUEST_FILTER);
    }

    private void initVar(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            timeFilter = savedInstanceState.getString(ARGS_TIME_FILTER, "");
            scoreFilter = savedInstanceState.getString(ARGS_SCORE_FILTER, "");
        } else {
            timeFilter = "";
            scoreFilter = "";
        }

        InboxReputationTypeFactory typeFactory = new InboxReputationTypeFactoryImpl(this);
        adapter = new InboxReputationAdapter(typeFactory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View parentView = inflater.inflate(R.layout.fragment_inbox_reputation, container, false);
        mainList = (RecyclerView) parentView.findViewById(R.id.review_list);
        swipeToRefresh = (SwipeToRefresh) parentView.findViewById(R.id.swipe_refresh_layout);
        searchView = (SearchView) parentView.findViewById(R.id.search);
        filterButton = parentView.findViewById(R.id.filter_button);
        prepareView();
        presenter.attachView(this);
        return parentView;
    }

    private void prepareView() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mainList.setLayoutManager(layoutManager);
        mainList.setAdapter(adapter);

        mainList.addOnScrollListener(onScroll());
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });

        setSearchviewTextSize(searchView, 12);
        setQueryHint();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.getFilteredInboxReputation(query,
                        timeFilter,
                        scoreFilter,
                        getTab());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    setQueryHint();
                    presenter.getFilteredInboxReputation("",
                            timeFilter,
                            scoreFilter,
                            getTab());
                }
                return false;
            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilter();
            }
        });
    }

    private void setSearchviewTextSize(SearchView searchView, int fontSize) {
        try {
            AutoCompleteTextView autoCompleteTextViewSearch = (AutoCompleteTextView) searchView.findViewById(searchView.getContext().getResources().getIdentifier("app:id/search_src_text", null, null));
            if (autoCompleteTextViewSearch != null) {
                autoCompleteTextViewSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            } else {
                LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
                LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
                LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
                AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
                autoComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            }
        } catch (Exception e) {
            LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
            LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
            LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
            AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
            autoComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        }
    }

    private void setQueryHint() {
        if (getTab() == InboxReputationActivity.TAB_BUYER_REVIEW) {
            searchView.setQueryHint(getString(R.string.query_hint_review_seller));
        } else {
            searchView.setQueryHint(getString(R.string.query_hint_review_buyer));
        }
    }

    public void refreshPage() {
        if (!swipeToRefresh.isRefreshing())
            showRefreshing();
        presenter.refreshPage(getQuery(),
                timeFilter, scoreFilter, getTab());
    }

    private RecyclerView.OnScrollListener onScroll() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                if (!adapter.isLoading() && !adapter.isEmpty())
                    presenter.getNextPage(lastItemPosition, visibleItem,
                            searchView.getQuery().toString(), timeFilter, scoreFilter, getTab());
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        KeyboardHandler.hideSoftKeyboard(getActivity());
        if (savedInstanceState != null)
            presenter.getFilteredInboxReputation(
                    savedInstanceState.getString(ARGS_QUERY, ""),
                    savedInstanceState.getString(ARGS_TIME_FILTER, ""),
                    savedInstanceState.getString(ARGS_SCORE_FILTER, ""),
                    getTab()
            );
        else {
            presenter.getFirstTimeInboxReputation(getTab());
        }
    }


    public int getTab() {
        if (getArguments() != null)
            return getArguments().getInt(PARAM_TAB, 1);
        else
            return -1;
    }

    @Override
    public void showLoadingFull() {
        adapter.showLoadingFull();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorGetFirstTimeInboxReputation(String errorMessage) {
        if (getActivity() != null & getView() != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new
                    NetworkErrorHelper
                            .RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            presenter.getFirstTimeInboxReputation(getTab());
                        }
                    });
        }
    }

    @Override
    public void onSuccessGetFirstTimeInboxReputation(InboxReputationViewModel inboxReputationViewModel) {
        searchView.setVisibility(View.VISIBLE);
        filterButton.setVisibility(View.VISIBLE);
        adapter.setList(inboxReputationViewModel.getList());
        presenter.setHasNextPage(inboxReputationViewModel.isHasNextPage());
    }

    @Override
    public void finishLoadingFull() {
        adapter.removeLoadingFull();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorGetNextPage(String errorMessage) {
        adapter.removeLoading();
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), errorMessage, new
                NetworkErrorHelper
                        .RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getFirstTimeInboxReputation(getTab());
                    }
                })
                .showRetrySnackbar();
    }

    @Override
    public void onSuccessGetNextPage(InboxReputationViewModel inboxReputationViewModel) {
        adapter.removeLoading();
        adapter.addList(inboxReputationViewModel.getList());
        presenter.setHasNextPage(inboxReputationViewModel.isHasNextPage());
    }

    @Override
    public void onErrorRefresh(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new
                NetworkErrorHelper
                        .RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.refreshPage(getQuery(), timeFilter, scoreFilter, getTab());
                    }
                });
    }

    @Override
    public void onSuccessRefresh(InboxReputationViewModel inboxReputationViewModel) {
        adapter.removeEmpty();
        adapter.setList(inboxReputationViewModel.getList());
        presenter.setHasNextPage(inboxReputationViewModel.isHasNextPage());
    }

    @Override
    public void showLoadingNext() {
        adapter.showLoading();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void finishLoading() {
        adapter.removeLoading();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGoToDetail(String reputationId, String invoice, String createTime,
                             String revieweeName, String revieweeImage,
                             ReputationDataViewModel reputationDataViewModel, String textDeadline,
                             int adapterPosition, int role) {
        savePassModelToDB(getInboxReputationDetailPassModel(reputationId, invoice, createTime,
                revieweeImage, revieweeName, textDeadline,
                reputationDataViewModel, role));

        startActivityForResult(
                InboxReputationDetailActivity.getCallingIntent(
                        getActivity(),
                        adapterPosition,
                        getTab()),
                REQUEST_OPEN_DETAIL);
    }

    private void savePassModelToDB(InboxReputationDetailPassModel inboxReputationDetailPassModel) {
        if (cacheManager != null) {
            cacheManager.setKey(InboxReputationDetailActivity.CACHE_PASS_DATA);
            cacheManager.setValue(CacheUtil.convertModelToString(inboxReputationDetailPassModel,
                    new TypeToken<InboxReputationDetailPassModel>() {
                    }.getType()));
            cacheManager.store();
        }
    }

    private void removeCachePassData() {
        if (cacheManager != null) {
            cacheManager.delete(InboxReputationDetailActivity.CACHE_PASS_DATA);
            cacheManager.store();
        }
    }


    private InboxReputationDetailPassModel getInboxReputationDetailPassModel(
            String reputationId,
            String invoice,
            String createTime,
            String revieweeImage,
            String revieweeName,
            String textDeadline,
            ReputationDataViewModel reputationDataViewModel,
            int role) {
        return new InboxReputationDetailPassModel(reputationId, revieweeName, revieweeImage,
                textDeadline, invoice, createTime, reputationDataViewModel, role);

    }

    @Override
    public void showRefreshing() {
        swipeToRefresh.setRefreshing(true);
    }

    @Override
    public void onSuccessGetFilteredInboxReputation(InboxReputationViewModel inboxReputationViewModel) {
        adapter.removeEmpty();
        adapter.setList(inboxReputationViewModel.getList());
        presenter.setHasNextPage(inboxReputationViewModel.isHasNextPage());
    }

    @Override
    public void onErrorGetFilteredInboxReputation(String errorMessage) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getFilteredInboxReputation(getQuery(), timeFilter, scoreFilter, getTab());
            }
        }).showRetrySnackbar();
    }

    @Override
    public void finishRefresh() {
        swipeToRefresh.setRefreshing(false);
    }

    @Override
    public void onShowEmpty() {
        searchView.setVisibility(View.GONE);
        filterButton.setVisibility(View.GONE);
        adapter.clearList();
        if (GlobalConfig.isSellerApp()
                || getTab() == InboxReputationActivity.TAB_BUYER_REVIEW) {
            adapter.showEmpty(getString(R.string.inbox_reputation_seller_empty_title));
        } else {
            adapter.showEmpty(getString(R.string.inbox_reputation_empty_title),
                    getString(R.string.inbox_reputation_empty_button),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToHotlist();
                        }
                    });
        }
        adapter.notifyDataSetChanged();
    }

    private void goToHotlist() {
        if (MainApplication.getAppContext() instanceof TkpdCoreRouter
                && !GlobalConfig.isSellerApp()) {
            Intent intent = ((TkpdCoreRouter) MainApplication.getAppContext()).getHomeHotlistIntent
                    (getActivity());
            startActivity(intent);
            getActivity().finish();
        } else if (MainApplication.getAppContext() instanceof TkpdCoreRouter) {
            Intent intent = ((TkpdCoreRouter) MainApplication.getAppContext()).getHomeIntent
                    (getActivity());
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onShowEmptyFilteredInboxReputation() {
        adapter.clearList();
        adapter.showEmpty(getString(R.string.inbox_reputation_search_empty_title),
                getString(R.string.inbox_reputation_search_empty_button),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeFilter = "";
                        scoreFilter = "";
                        searchView.setQuery("", true);

                    }
                });
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OPEN_DETAIL) {
            removeCachePassData();
            if (resultCode == Activity.RESULT_OK) refreshPage();
        } else if (requestCode == REQUEST_FILTER
                && resultCode == Activity.RESULT_OK
                && data != null) {
            timeFilter = data.getExtras().getString(
                    InboxReputationFilterFragment.SELECTED_TIME_FILTER, "");
            scoreFilter = data.getExtras().getString(InboxReputationFilterFragment
                    .SELECTED_SCORE_FILTER, "");
            presenter.getFilteredInboxReputation(
                    getQuery(),
                    timeFilter,
                    scoreFilter,
                    getTab()
            );
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private String getQuery() {
        if (searchView != null)
            return searchView.getQuery().toString();
        else
            return "";
    }

    @Override
    public void onResume() {
        super.onResume();
        KeyboardHandler.DropKeyboard(getActivity(), searchView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARGS_TIME_FILTER, timeFilter);
        outState.putString(ARGS_SCORE_FILTER, scoreFilter);
        outState.putString(ARGS_QUERY, getQuery());
    }
}
