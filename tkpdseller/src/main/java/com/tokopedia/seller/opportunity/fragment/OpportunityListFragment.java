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
import com.tokopedia.core.discovery.model.Sort;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.SortActivity;
import com.tokopedia.seller.opportunity.adapter.OpportunityListAdapter;
import com.tokopedia.seller.opportunity.listener.OpportunityListView;
import com.tokopedia.seller.opportunity.presenter.OpportunityListPresenter;
import com.tokopedia.seller.opportunity.presenter.OpportunityListPresenterImpl;
import com.tokopedia.seller.opportunity.viewmodel.OpportunityItemViewModel;
import com.tokopedia.seller.opportunity.viewmodel.OpportunityListPageViewModel;
import com.tokopedia.seller.opportunity.viewmodel.SortingTypeViewModel;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;

import java.util.ArrayList;

/**
 * Created by nisie on 3/1/17.
 */

public class OpportunityListFragment extends BasePresenterFragment<OpportunityListPresenter>
        implements OpportunityListView {

    private static final int REQUEST_OPEN_DETAIL = 123;
    private static final String CACHE_SEEN_OPPORTUNITY = "CACHE_SEEN_OPPORTUNITY";
    private static final java.lang.String HAS_SEEN_OPPORTUNITY = "HAS_SEEN_OPPORTUNITY";
    RecyclerView opportunityList;
    TextView headerInfo;
    SearchView searchView;
    View filterButton;
    View sortButton;

    OpportunityListAdapter adapter;
    LinearLayoutManager layoutManager;
    RefreshHandler refreshHandler;

    LocalCacheHandler cacheHandler;

    public static Fragment newInstance() {
        return new OpportunityListFragment();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        KeyboardHandler.DropKeyboard(getActivity(), searchView);
        presenter.getOpportunity();
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
        presenter = new OpportunityListPresenterImpl(this
        );
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
                presenter.onRefresh();
            }
        };
    }

    private OpportunityListAdapter.OpportunityListener onGoToDetail() {
        return new OpportunityListAdapter.OpportunityListener() {
            @Override
            public void goToDetail(OpportunityItemViewModel opportunityItemViewModel) {
//                startActivityForResult(OppurtunityDetailActivity.getDetailIntent(getActivity(), opportunity),
//                        REQUEST_OPEN_DETAIL);
            }
        };
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
                        && lastItemPosition == visibleItem)
                    presenter.loadMore();
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        }
    }

    @Override
    public void onSuccessGetOpportunity(OpportunityListPageViewModel viewModel) {
        finishLoadingList();
        finishRefresh();
        adapter.setList(viewModel.getOpportunityViewModel().getListOpportunity());

        setFooter(viewModel);
    }

    private void setFooter(OpportunityListPageViewModel viewModel) {
        setFilter(viewModel);
        setSort(viewModel);
    }

    private void setSort(OpportunityListPageViewModel viewModel) {

    }

    private void setFilter(OpportunityListPageViewModel viewModel) {
        final ArrayList<String> listSort = new ArrayList<>();
        for(SortingTypeViewModel sortItem : viewModel.getListSortingType()){
            listSort.add(sortItem.getSortingTypeName());
        }

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SortActivity.class);
                intent.putExtra(SortFragment.EXTRA_LIST_SORT, listSort);
                startActivityForResult(intent, SortFragment.REQUEST_SORT);
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

        finishRefresh();
        if (errorMessage.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
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

    private void finishLoadingList() {
        if (adapter.isLoading()) {
            adapter.showLoading(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OPEN_DETAIL && resultCode == Activity.RESULT_OK) {

        } else if(requestCode == SortFragment.REQUEST_SORT && resultCode == Activity.RESULT_OK) {
            CommonUtils.dumper("NISNIS" + data.getExtras().getInt(SortFragment.SELECTED_POSITION));
            setSortActive();
        }else{
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void setSortActive() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
        cacheHandler = null;
    }


}
