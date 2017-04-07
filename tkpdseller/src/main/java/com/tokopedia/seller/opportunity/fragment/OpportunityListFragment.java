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

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.database.model.PagingHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.OpportunityFilterActivity;
import com.tokopedia.seller.opportunity.OpportunitySortActivity;
import com.tokopedia.seller.opportunity.OpportunityDetailActivity;
import com.tokopedia.seller.opportunity.adapter.OpportunityListAdapter;
import com.tokopedia.seller.opportunity.listener.OpportunityListView;
import com.tokopedia.seller.opportunity.presenter.OpportunityListPresenter;
import com.tokopedia.seller.opportunity.presenter.OpportunityListPresenterImpl;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityFilterViewModel;
import com.tokopedia.seller.opportunity.viewmodel.SortingTypeViewModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityViewModel;

import java.util.ArrayList;

/**
 * Created by nisie on 3/1/17.
 */

public class OpportunityListFragment extends BasePresenterFragment<OpportunityListPresenter>
        implements OpportunityListView {

    public static final int REQUEST_SORT = 101;
    public static final int REQUEST_FILTER = 102;
    private static final int REQUEST_CODE_OPPORTUNITY_DETAIL = 2017;

    private static final String CACHE_SEEN_OPPORTUNITY = "CACHE_SEEN_OPPORTUNITY";
    private static final java.lang.String HAS_SEEN_OPPORTUNITY = "HAS_SEEN_OPPORTUNITY";

    private static final int DEFAULT_CATEGORY_SELECTED = 1;
    RecyclerView opportunityList;
    TextView headerInfo;
    SearchView searchView;
    View footer;
    View filterButton;
    View sortButton;

    private OpportunityListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RefreshHandler refreshHandler;
    private LocalCacheHandler cacheHandler;
    private PagingHandler pagingHandler;

    OpportunityFilterViewModel filterData;

    public static Fragment newInstance() {
        return new OpportunityListFragment();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {

        } else {
            filterData = new OpportunityFilterViewModel();
        }
    }

    @Override
    protected void onFirstTimeLaunched() {
        KeyboardHandler.DropKeyboard(getActivity(), searchView);
        presenter.getOpportunity();
        presenter.getFilter();
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
        presenter = new OpportunityListPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

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
        opportunityList = (RecyclerView) view.findViewById(R.id.opportunity_list);
        headerInfo = (TextView) view.findViewById(R.id.header_info);
        searchView = (SearchView) view.findViewById(R.id.search);
        filterButton = view.findViewById(R.id.filter);
        sortButton = view.findViewById(R.id.sort);
        footer = view.findViewById(R.id.footer);

        adapter = OpportunityListAdapter.createInstance(onGoToDetail());
        layoutManager = new LinearLayoutManager(getActivity());
        opportunityList.setLayoutManager(layoutManager);
        opportunityList.setAdapter(adapter);

        refreshHandler = new RefreshHandler(getActivity(), view, onRefresh());
        initHeaderText();
    }

    private void initHeaderText() {
        cacheHandler = new LocalCacheHandler(getActivity(), CACHE_SEEN_OPPORTUNITY);
        if (cacheHandler.getBoolean(HAS_SEEN_OPPORTUNITY, false)) {
            headerInfo.setVisibility(View.GONE);
        } else {
            headerInfo.setVisibility(View.VISIBLE);
            cacheHandler.putBoolean(HAS_SEEN_OPPORTUNITY, true);
        }
    }

    private RefreshHandler.OnRefreshHandlerListener onRefresh() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                pagingHandler.resetPage();
                presenter.getOpportunity();
            }
        };
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
        opportunityList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                if (!refreshHandler.isRefreshing()
                        && adapter.getList().size() != 0
                        && lastItemPosition == visibleItem
                        && !adapter.isLoading()
                        && hasNextPage())
                    presenter.getOpportunity();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.setParamQuery(query);
                presenter.getOpportunity();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    presenter.setParamQuery("");
                    presenter.getOpportunity();
                }
                return false;
            }
        });

    }

    @Override
    protected void initialVar() {
        pagingHandler = new PagingHandler();
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showLoadingList() {
        if (!refreshHandler.isRefreshing() && (adapter.getList().size() == 0 || adapter.isEmpty())) {
            adapter.showLoadingFull(true);
        } else if (!refreshHandler.isRefreshing()) {
            adapter.showLoading(true);
            opportunityList.smoothScrollToPosition(adapter.getItemCount());
        }
    }

    @Override
    public void onSuccessGetOpportunity(OpportunityViewModel viewModel) {
        setPaging(viewModel.getPagingHandlerModel());
        finishLoadingList();
        finishRefresh();
        adapter.showEmpty(false);
        adapter.setList(viewModel.getListOpportunity());
        if (adapter.getList().size() == 0) {
            adapter.showEmptyFull(true);
        }
    }

    private void setPaging(PagingHandler.PagingHandlerModel pagingModel) {
        if (pagingHandler.getPage() == 1) {
            adapter.getList().clear();
        }
        pagingHandler.setHasNext(checkHasNext(pagingModel));
    }

    private boolean checkHasNext(PagingHandler.PagingHandlerModel pagingHandlerModel) {
        return !pagingHandlerModel.getUriNext().equals("0") && !pagingHandlerModel.getUriNext().equals("");
    }

    private void setSort(OpportunityFilterViewModel viewModel) {

        final ArrayList<String> listSort = new ArrayList<>();
        for (SortingTypeViewModel sortItem : viewModel.getListSortingType()) {
            listSort.add(sortItem.getSortingTypeName());
        }

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OpportunitySortActivity.class);
                intent.putExtra(OpportunitySortFragment.EXTRA_LIST_SORT, listSort);
                startActivityForResult(intent, REQUEST_SORT);
            }
        });

    }

    private void setFilter(OpportunityFilterViewModel viewModel) {
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OpportunityFilterActivity.class);
                intent.putExtra(OpportunityFilterActivity.EXTRA_CATEGORY_SELECTED_VALUE, getSelectedFilter());
                startActivityForResult(intent, REQUEST_FILTER);
            }
        });
    }

    private int getSelectedFilter() {
        return presenter.getPass().getCategory() != null ? Integer.parseInt(presenter.getPass().getCategory()) : DEFAULT_CATEGORY_SELECTED;
    }

    private void finishRefresh() {
        if (refreshHandler != null && refreshHandler.isRefreshing())
            refreshHandler.finishRefresh();
    }

    @Override
    public void onErrorGetOpportunity(String errorMessage) {
        finishLoadingList();
        finishRefresh();

        if (adapter.getList().size() == 0 && errorMessage.equals("")) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), onRetry());
        } else if (adapter.getList().size() == 0) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, onRetry());
        } else if (errorMessage.equals("")) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    private NetworkErrorHelper.RetryClickedListener onRetry() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getOpportunity();
            }
        };
    }

    @Override
    public OpportunityListAdapter getAdapter() {
        return adapter;
    }

    @Override
    public String getSortParam() {
        return "";
    }

    @Override
    public String getShippingParam() {
        return "";
    }

    @Override
    public String getCategoryParam() {
        return "";
    }

    @Override
    public boolean isFilterEmpty() {
        return filterData != null
                && (
                (filterData.getListCategory() == null || (filterData != null && filterData.getListCategory().size() == 0))
                        || (filterData.getListSortingType() == null || (filterData != null && filterData.getListSortingType().size() == 0))
                        || (filterData.getListShippingType() == null || (filterData != null && filterData.getListShippingType().size() == 0))
        );
    }

    @Override
    public int getPage() {
        return pagingHandler.getPage();
    }

    @Override
    public void onSuccessGetFilter(OpportunityFilterViewModel opportunityFilterViewModel) {
        footer.setVisibility(View.VISIBLE);
        setFilter(opportunityFilterViewModel);
        setSort(opportunityFilterViewModel);
    }

    @Override
    public void onErrorGetFilter(String errorMessage) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getFilter();
            }
        }).showRetrySnackbar();
    }

    private void finishLoadingList() {
        if (adapter.isLoading()) {
            adapter.showLoading(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_OPPORTUNITY_DETAIL && resultCode == Activity.RESULT_OK) {
            // refresh the list

        } else if (requestCode == REQUEST_CODE_OPPORTUNITY_DETAIL
                && resultCode == OpportunityDetailFragment.RESULT_DELETED
                && data != null) {
            OpportunityItemViewModel viewModel = data.getParcelableExtra(
                    OpportunityDetailActivity.OPPORTUNITY_EXTRA_PARAM);
            adapter.getList().remove(viewModel.getPosition());
            if (adapter.getList().size() == 0)
                adapter.showEmptyFull(true);
            adapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_SORT && resultCode == Activity.RESULT_OK) {
            CommonUtils.dumper("NISNIS Sort" + data.getExtras().getInt(OpportunitySortFragment.SELECTED_POSITION));
            setSortActive();
        } else if (requestCode == REQUEST_FILTER && resultCode == Activity.RESULT_OK) {
            CommonUtils.dumper("NISNIS Category" + data.getExtras().getInt(OpportunityCategoryFragment.SELECTED_POSITION));
            setSortActive();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setSortActive() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unsubscribeObservable();
        cacheHandler = null;
    }


}
