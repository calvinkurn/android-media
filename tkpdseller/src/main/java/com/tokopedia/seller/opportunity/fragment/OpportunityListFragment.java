package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.model.PagingHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.showcase.ShowCaseDialogFactory;
import com.tokopedia.seller.opportunity.activity.OpportunityDetailActivity;
import com.tokopedia.seller.opportunity.activity.OpportunityFilterActivity;
import com.tokopedia.seller.opportunity.activity.OpportunitySortActivity;
import com.tokopedia.seller.opportunity.adapter.OpportunityListAdapter;
import com.tokopedia.seller.opportunity.analytics.OpportunityTrackingEventLabel;
import com.tokopedia.seller.opportunity.di.component.OpportunityComponent;
import com.tokopedia.seller.opportunity.di.module.OpportunityModule;
import com.tokopedia.seller.opportunity.domain.param.GetOpportunityListParam;
import com.tokopedia.seller.opportunity.listener.OpportunityListView;
import com.tokopedia.seller.opportunity.presenter.OpportunityListPresenter;
import com.tokopedia.seller.opportunity.viewmodel.OpportunityFilterPassModel;
import com.tokopedia.seller.opportunity.viewmodel.SortingTypeViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.FilterPass;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityFilterViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityViewModel;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.seller.opportunity.di.component.DaggerOpportunityComponent;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.tokopedia.seller.opportunity.activity.OpportunityFilterActivity.CACHE_OPPORTUNITY_FILTER;

/**
 * Created by nisie on 3/1/17.
 */

public class OpportunityListFragment extends BasePresenterFragment<OpportunityListPresenter>
        implements OpportunityListView {

    public static final int REQUEST_SORT = 101;
    public static final int REQUEST_FILTER = 102;
    private static final int REQUEST_CODE_OPPORTUNITY_DETAIL = 2017;

    private static final String CACHE_SEEN_OPPORTUNITY = "CACHE_SEEN_OPPORTUNITY";
    private static final String HAS_SEEN_OPPORTUNITY = "HAS_SEEN_OPPORTUNITY";

    private static final String EXTRA_QUERY = "EXTRA_QUERY";

    private static final String ARGS_FILTER_DATA = "ARGS_FILTER_DATA";
    private static final String ARGS_PARAM = "ARGS_PARAM";

    RecyclerView opportunityList;
    TextView headerInfo;
    SearchView searchView;
    View footer;
    View filterButton;
    View sortButton;
    TextView sortText;

    private OpportunityListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RefreshHandler refreshHandler;
    private LocalCacheHandler cacheHandler;
    private PagingHandler pagingHandler;
    private GlobalCacheManager cacheManager;

    private GetOpportunityListParam opportunityParam;
    private OpportunityFilterViewModel filterData;
    private OpportunityFilterPassModel opportunityFilterPassModel;

    private OpportunityComponent opportunityComponent;

    private ShowCaseDialog showCaseDialog;

    @Inject
    OpportunityListPresenter presenter;

    public static Fragment newInstance(String query) {
        Fragment fragment = new OpportunityListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_QUERY, query);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    public void startShowCase() {

        final String showCaseTag = OpportunityListFragment.class.getName();
        if (ShowCasePreference.hasShown(getActivity(), showCaseTag)) {
            return;
        }
        if (showCaseDialog != null) {
            return;
        }

        final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();

        if (opportunityList == null)
            return;

        opportunityList.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getView() == null) {
                    return;
                }

                View itemView = getItemRecyclerView();
                if (itemView != null && itemView.findViewById(R.id.reputation_point) != null) {
                    showCaseList.add(
                            new ShowCaseObject(
                                    itemView.findViewById(R.id.reputation_point),
                                    getString(R.string.opportunity_showcase_reputation_value_title),
                                    getString(R.string.opportunity_showcase_reputation_value_content),
                                    ShowCaseContentPosition.UNDEFINED));
                }

                if (showCaseList.isEmpty())
                    return;

                showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase();
                showCaseDialog.show(getActivity(), showCaseTag, showCaseList);
            }
        }, 300);
    }

    // get first item to start demo the opportunity
    public View getItemRecyclerView() {
        int position = layoutManager.findFirstCompletelyVisibleItemPosition();
        return layoutManager.findViewByPosition(position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null
                && savedInstanceState.getParcelable(ARGS_FILTER_DATA) != null) {
            filterData = savedInstanceState.getParcelable(ARGS_FILTER_DATA);
        } else {
            filterData = new OpportunityFilterViewModel();
        }

        if (savedInstanceState != null
                && savedInstanceState.getParcelable(ARGS_PARAM) != null)
            opportunityParam = savedInstanceState.getParcelable(ARGS_PARAM);
        else {
            opportunityParam = new GetOpportunityListParam();
            if (getArguments().containsKey(EXTRA_QUERY)) {
                opportunityParam.setQuery(getArguments().getString(EXTRA_QUERY));
            }
        }

    }

    @Override
    protected void onFirstTimeLaunched() {
        KeyboardHandler.DropKeyboard(getActivity(), searchView);
        searchView.setQuery(opportunityParam.getQuery(),false);
        presenter.initOpportunityForFirstTime(
                opportunityParam.getQuery(),
                opportunityParam.getListFilter()
        );
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        opportunityComponent.inject(this);
        presenter.attachView(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        if (activity != null && activity instanceof BaseActivity) {
            opportunityComponent = DaggerOpportunityComponent
                    .builder()
                    .opportunityModule(new OpportunityModule())
                    .appComponent(((BaseActivity) activity).getApplicationComponent())
                    .build();
        }
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_opportunity_list;
    }

    @Override
    protected void initView(View view) {
        opportunityList = view.findViewById(R.id.opportunity_list);
        headerInfo = view.findViewById(R.id.header_info);
        searchView = view.findViewById(R.id.search);
        filterButton = view.findViewById(R.id.filter);
        sortButton = view.findViewById(R.id.sort);
        sortText = view.findViewById(R.id.sort_but);
        footer = view.findViewById(R.id.footer);

        adapter = OpportunityListAdapter.createInstance(onGoToDetail());
        layoutManager = new LinearLayoutManager(getActivity());
        opportunityList.setLayoutManager(layoutManager);
        opportunityList.setAdapter(adapter);

        refreshHandler = new RefreshHandler(getActivity(), view, onRefresh());
        headerInfo.setVisibility(View.VISIBLE);
    }

    // TODO need confirm to put this logic when initView
    private void initHeaderText() {
        cacheHandler = new LocalCacheHandler(getActivity(), CACHE_SEEN_OPPORTUNITY);
        if (cacheHandler.getBoolean(HAS_SEEN_OPPORTUNITY, false)) {
            headerInfo.setVisibility(View.GONE);
        } else {
            headerInfo.setVisibility(View.VISIBLE);
            cacheHandler.putBoolean(HAS_SEEN_OPPORTUNITY, true);
            cacheHandler.applyEditor();
        }
    }

    private RefreshHandler.OnRefreshHandlerListener onRefresh() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                resetOpportunityList();
            }
        };
    }

    private void resetOpportunityList() {
        pagingHandler.resetPage();
        presenter.initOpportunityForFirstTime(
                opportunityParam.getQuery(),
                opportunityParam.getListFilter()
        );
    }


    private OpportunityListAdapter.OpportunityListener onGoToDetail() {
        return new OpportunityListAdapter.OpportunityListener() {
            @Override
            public void goToDetail(OpportunityItemViewModel opportunityItemViewModel) {
                Intent intent = OpportunityDetailActivity.createIntent(getActivity(), opportunityItemViewModel);
                startActivityForResult(intent, REQUEST_CODE_OPPORTUNITY_DETAIL);
            }
        };
    }

    private boolean hasNextPage() {
        return pagingHandler.CheckNextPage();
    }

    @Override
    protected void setViewListener() {
        opportunityList.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                        int visibleItem = layoutManager.getItemCount() - 1;
                        if (!refreshHandler.isRefreshing()
                                && adapter.getList().size() != 0
                                && lastItemPosition == visibleItem
                                && !adapter.isLoading()
                                && hasNextPage()) {
                            pagingHandler.nextPage();
                            presenter.getOpportunity(
                                    opportunityParam.getQuery(),
                                    opportunityParam.getListFilter());
                        }
                    }
                });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                opportunityParam.setQuery(query);
                resetOpportunityList();

                UnifyTracking.eventOpportunity(
                        OpportunityTrackingEventLabel.EventName.SUBMIT_OPPORTUNITY,
                        OpportunityTrackingEventLabel.EventCategory.OPPORTUNITY_FILTER,
                        AppEventTracking.Action.SUBMIT,
                        query
                );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    opportunityParam.setQuery("");
                    resetOpportunityList();
                }
                return false;
            }
        });

    }

    @Override
    protected void initialVar() {
        pagingHandler = new PagingHandler();
        cacheManager = new GlobalCacheManager();
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showLoadingList() {
        if (pagingHandler.getPage() == 1 && adapter.getList().size() > 0) {
            refreshHandler.setRefreshing(true);
        } else if (!refreshHandler.isRefreshing() && (adapter.getList().size() == 0 || adapter.isEmpty())) {
            adapter.showLoadingFull(true);
        } else if (!refreshHandler.isRefreshing()) {
            adapter.showLoading(true);
            opportunityList.smoothScrollToPosition(adapter.getItemCount());
        }
    }

    @Override
    public void onSuccessGetOpportunity(OpportunityViewModel viewModel) {
        setPaging(viewModel.getPagingHandlerModel());

        UnifyTracking.eventOpportunity(
                OpportunityTrackingEventLabel.EventName.LOAD_OPPORTUNITY_PRODUCT,
                OpportunityTrackingEventLabel.EventCategory.OPPORTUNITY_FILTER,
                AppEventTracking.Action.LOAD,
                String.valueOf(pagingHandler.getPage())
        );

        enableView();
        finishLoadingList();
        adapter.setList(viewModel.getListOpportunity());

    }

    private void setPaging(PagingHandler.PagingHandlerModel pagingModel) {
        pagingHandler.setHasNext(checkHasNext(pagingModel));
    }

    private boolean checkHasNext(PagingHandler.PagingHandlerModel pagingHandlerModel) {
        return !pagingHandlerModel.getUriNext().equals("0") && !pagingHandlerModel.getUriNext().equals("");
    }

    private void setSort() {

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<SortingTypeViewModel> listSort = new ArrayList<>(filterData.getListSortingType());
                startActivityForResult(
                        OpportunitySortActivity.getCallingIntent(
                                getActivity(),
                                listSort)
                        , REQUEST_SORT);
            }
        });

    }

    private void setFilter() {
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                opportunityFilterPassModel = new OpportunityFilterPassModel();
                opportunityFilterPassModel.setListFilter(filterData.getListFilter());

                cacheManager.setKey(CACHE_OPPORTUNITY_FILTER);
                cacheManager.setValue(CacheUtil.convertModelToString(opportunityFilterPassModel,
                        new TypeToken<OpportunityFilterPassModel>() {
                        }.getType()));
                cacheManager.store();

                Intent intent = OpportunityFilterActivity.createIntent(getActivity());
                startActivityForResult(intent, REQUEST_FILTER);
            }
        });
    }

    private void finishRefresh() {
        if (refreshHandler != null && refreshHandler.isRefreshing())
            refreshHandler.finishRefresh();
    }

    @Override
    public void onErrorGetOpportunity(String errorMessage) {
        finishLoadingList();
        enableView();
        if (errorMessage.equals("")) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public OpportunityListAdapter getAdapter() {
        return adapter;
    }

    @Override
    public boolean isFilterEmpty() {
        return filterData != null
                && ((filterData.getListFilter() == null ||
                (filterData != null && filterData.getListFilter().size() == 0))
                || (filterData.getListSortingType() == null ||
                (filterData != null && filterData.getListSortingType().size() == 0)));
    }

    @Override
    public int getPage() {
        return pagingHandler.getPage();
    }

    @Override
    public void onErrorFirstTime(String errorMessage) {
        finishLoadingList();
        finishRefresh();

        if (adapter.getList().size() == 0 && errorMessage.equals("")) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), onRetryFirstTime());
        } else if (adapter.getList().size() == 0) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, onRetryFirstTime());
        } else if (errorMessage.equals("")) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);

    }

    private NetworkErrorHelper.RetryClickedListener onRetryFirstTime() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.initOpportunityForFirstTime(
                        opportunityParam.getQuery(),
                        opportunityParam.getListFilter()
                );
            }
        };
    }

    @Override
    public void onSuccessFirstTime(OpportunityViewModel opportunityViewModel,
                                   OpportunityFilterViewModel opportunityFilterViewModel) {
        finishLoadingList();
        finishRefresh();
        setPaging(opportunityViewModel.getPagingHandlerModel());
        adapter.showEmptyFull(false);
        adapter.getList().clear();
        adapter.setList(opportunityViewModel.getListOpportunity());
        if (adapter.getList().size() == 0) {
            adapter.showEmptyFull(true);
        }

        if (filterData.getListFilter() == null
                && filterData.getListSortingType() == null) {
            filterData = opportunityFilterViewModel;
        }

        setFilter();
        setSort();

        enableView();

        if (!getUserVisibleHint())
            return;

        startShowCase();
    }

    private void enableView() {
        if (!isFilterEmpty()) {
            searchView.setVisibility(View.VISIBLE);
            footer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void disableView() {
        searchView.setVisibility(View.GONE);
        footer.setVisibility(View.GONE);
    }

    private void finishLoadingList() {
        if (adapter.isLoading()) {
            adapter.showLoading(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ScreenTracking.screen(getScreenName());

        if (requestCode == REQUEST_CODE_OPPORTUNITY_DETAIL
                && resultCode == OpportunityDetailFragment.RESULT_DELETED
                && data != null) {
            OpportunityItemViewModel viewModel = data.getParcelableExtra(
                    OpportunityDetailActivity.OPPORTUNITY_EXTRA_PARAM);
            adapter.getList().remove(viewModel.getPosition());
            if (adapter.getList().size() == 0)
                adapter.showEmptyFull(true);
            adapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE_OPPORTUNITY_DETAIL
                && resultCode == Activity.RESULT_OK
                && data != null && data.getBooleanExtra(OpportunityTncFragment.ACCEPTED_OPPORTUNITY, false)) {
            resetOpportunityList();
        } else if (requestCode == REQUEST_SORT && resultCode == Activity.RESULT_OK) {
            setOpportunitySortData(data);
        } else if (requestCode == REQUEST_FILTER && resultCode == Activity.RESULT_OK) {
            setOpportunityFilterData();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setOpportunityFilterData() {

        opportunityFilterPassModel =
                cacheManager.getConvertObjData(CACHE_OPPORTUNITY_FILTER,
                        OpportunityFilterPassModel.class);
        filterData.setListFilter(opportunityFilterPassModel.getListFilter());

        ArrayList<FilterPass> listPass = opportunityFilterPassModel.getListPass();

        if (listPass != null) {
            opportunityParam.setFilter(listPass);
            resetOpportunityList();
        }
    }

    private void setOpportunitySortData(Intent data) {
        if (filterData != null && filterData.getListSortingType() != null) {
            FilterPass sort = data.getExtras().getParcelable(OpportunitySortFragment.SELECTED_SORT);
            int position = data.getExtras().getInt(OpportunitySortFragment.SELECTED_POSITION);

            for (int i = 0; i < filterData.getListSortingType().size(); i++) {
                if (i != position)
                    filterData.getListSortingType().get(i).setSelected(false);
                else {
                    filterData.getListSortingType().get(i).setSelected(true);
                }
            }
            opportunityParam.setSort(sort);
            resetOpportunityList();
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unsubscribeObservable();
        cacheHandler = null;
        cacheManager = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARGS_FILTER_DATA, filterData);
        outState.putParcelable(ARGS_PARAM, opportunityParam);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_OPPORTUNITY_TAB;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getActivity() != null) {
            ScreenTracking.screen(getScreenName());

            startShowCase();
        }
    }
}
