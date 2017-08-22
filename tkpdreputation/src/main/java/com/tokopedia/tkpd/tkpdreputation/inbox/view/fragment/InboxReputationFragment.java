package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationDetailActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFilterActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.InboxReputationAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox.InboxReputationTypeFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox.InboxReputationTypeFactoryImpl;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.presenter.InboxReputationPresenter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.FilterPass;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.FilterPassModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.FilterViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.OptionViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFilterActivity.CACHE_INBOX_REPUTATION_FILTER;


/**
 * @author by nisie on 8/11/17.
 */

public class InboxReputationFragment extends BaseDaggerFragment
        implements InboxReputation.View {

    private final static String PARAM_TAB = "tab";
    private static final String ARGS_FILTER_DATA = "ARGS_FILTER_DATA";
    private static final int REQUEST_OPEN_DETAIL = 101;
    private static final int REQUEST_FILTER = 102;
    private static final String FILTER_ALL_TIME = "1";
    private static final String FILTER_LAST_WEEK = "2";
    private static final String FILTER_THIS_MONTH = "3";
    private static final String FILTER_LAST_3_MONTH = "4";
    private static final String FILTER_GIVEN_REPUTATION = "2";

    private RecyclerView mainList;
    private SwipeToRefresh swipeToRefresh;
    private LinearLayoutManager layoutManager;
    private InboxReputationAdapter adapter;
    private InboxReputationFilterViewModel filterData;
    private FilterPassModel filterPassModel;

    @Inject
    InboxReputationPresenter presenter;

    @Inject
    GlobalCacheManager cacheManager;

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
        if (savedInstanceState != null
                && savedInstanceState.getParcelable(ARGS_FILTER_DATA) != null) {
            filterData = savedInstanceState.getParcelable(ARGS_FILTER_DATA);
        } else {
            filterData = new InboxReputationFilterViewModel(createListFilter());
        }
        initVar();
    }

    private ArrayList<FilterViewModel> createListFilter() {
        ArrayList<FilterViewModel> listFilter = new ArrayList<>();
        listFilter.add(createFilterTime());
        listFilter.add(createFilterStatus());

        return listFilter;
    }

    private FilterViewModel createFilterStatus() {
        FilterViewModel statusFilter = new FilterViewModel(
                getString(R.string.filter_status),
                createListFilterStatus());
        return statusFilter;
    }

    private ArrayList<OptionViewModel> createListFilterStatus() {
        ArrayList<OptionViewModel> list = new ArrayList<OptionViewModel>();
        list.add(new OptionViewModel(getString(R.string.filter_all),
                GetFirstTimeInboxReputationUseCase.PARAM_STATUS, "1", list.size()));
        list.add(new OptionViewModel(getString(R.string.filter_given_reputation),
                GetFirstTimeInboxReputationUseCase.PARAM_STATUS, FILTER_GIVEN_REPUTATION, list.size
                ()));
        list.add(new OptionViewModel(getString(R.string.filter_no_reputation),
                GetFirstTimeInboxReputationUseCase.PARAM_STATUS, "3", list.size()));
        return list;
    }

    private FilterViewModel createFilterTime() {
        FilterViewModel timeFilter = new FilterViewModel(
                getString(R.string.filter_time),
                createListFilterTime());
        return timeFilter;
    }

    private ArrayList<OptionViewModel> createListFilterTime() {
        ArrayList<OptionViewModel> list = new ArrayList<OptionViewModel>();
        list.add(new OptionViewModel(getString(R.string.filter_all_time),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_ALL_TIME, list.size
                ()));
        list.add(new OptionViewModel(getString(R.string.filter_last_7_days),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_LAST_WEEK, list.size
                ()));
        list.add(new OptionViewModel(getString(R.string.filter_this_month),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_THIS_MONTH, list.size
                ()));
        list.add(new OptionViewModel(getString(R.string.filter_last_3_month),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_LAST_3_MONTH, list.size
                ()));
        return list;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filter_inbox_reputation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            openFilter();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void openFilter() {

        filterPassModel = new FilterPassModel();
        filterPassModel.setListFilter(filterData.getListFilter());

        cacheManager.setKey(CACHE_INBOX_REPUTATION_FILTER);
        cacheManager.setValue(CacheUtil.convertModelToString(filterPassModel,
                new TypeToken<FilterPassModel>() {
                }.getType()));
        cacheManager.store();

        Intent intent = InboxReputationFilterActivity.createIntent(getActivity());
        startActivityForResult(intent, REQUEST_FILTER);
    }

    private void initVar() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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
        prepareView();
        presenter.attachView(this);
        return parentView;
    }

    private void prepareView() {
        mainList.setLayoutManager(layoutManager);
        mainList.setAdapter(adapter);

        mainList.addOnScrollListener(onScroll());
        swipeToRefresh.setOnRefreshListener(onRefresh());
    }

    private SwipeRefreshLayout.OnRefreshListener onRefresh() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshPage(getTab());
            }
        };
    }

    private RecyclerView.OnScrollListener onScroll() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                if (!adapter.isLoading())
                    presenter.getNextPage(lastItemPosition, visibleItem, "",
                            filterPassModel.getListPass(), getTab
                                    ());
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getFirstTimeInboxReputation(getTab());
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
    }

    @Override
    public void onErrorGetFirstTimeInboxReputation(String errorMessage) {
        adapter.removeLoadingFull();
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new
                NetworkErrorHelper
                        .RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getFirstTimeInboxReputation(getTab());
                    }
                });
    }

    @Override
    public void onSuccessGetFirstTimeInboxReputation(InboxReputationViewModel inboxReputationViewModel) {
        adapter.removeLoadingFull();
        adapter.setList(inboxReputationViewModel.getList());
        presenter.setHasNextPage(inboxReputationViewModel.isHasNextPage());
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
    }

    @Override
    public void onErrorRefresh(String errorMessage) {
        swipeToRefresh.setRefreshing(false);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new
                NetworkErrorHelper
                        .RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.refreshPage(getTab());
                    }
                });
    }

    @Override
    public void onSuccessRefresh(InboxReputationViewModel inboxReputationViewModel) {
        swipeToRefresh.setRefreshing(false);
        adapter.setList(inboxReputationViewModel.getList());
        presenter.setHasNextPage(inboxReputationViewModel.isHasNextPage());
    }

    @Override
    public void showLoadingNext() {
        adapter.showLoading();
    }

    @Override
    public void onGoToDetail(String reputationId, int adapterPosition) {
        startActivityForResult(
                InboxReputationDetailActivity.getCallingIntent(
                        getActivity(),
                        reputationId,
                        adapterPosition,
                        getTab()),
                REQUEST_OPEN_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OPEN_DETAIL && resultCode == Activity.RESULT_OK) {
            presenter.refreshItem(
                    data.getStringExtra(InboxReputationDetailActivity.ARGS_REPUTATION_ID),
                    getTab());
        } else if (requestCode == REQUEST_FILTER && resultCode == Activity.RESULT_OK) {
            filterPassModel =
                    cacheManager.getConvertObjData(InboxReputationFilterActivity
                                    .CACHE_INBOX_REPUTATION_FILTER,
                            FilterPassModel.class);
            filterData.setListFilter(filterPassModel.getListFilter());

            ArrayList<FilterPass> listPass = filterPassModel.getListPass();

            if (listPass != null) {
                presenter.getFilteredInboxReputation(
                        getQuery(),
                        listPass,
                        getTab()
                );
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getQuery() {
        return "";
    }
}
