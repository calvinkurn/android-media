package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.opportunity.OppurtunityDetailActivity;
import com.tokopedia.seller.opportunity.adapter.OpportunityListAdapter;
import com.tokopedia.seller.opportunity.interactor.OpportunityNetworkInteractorImpl;
import com.tokopedia.seller.opportunity.listener.OpportunityListView;
import com.tokopedia.seller.opportunity.presenter.OpportunityListPresenter;
import com.tokopedia.seller.opportunity.presenter.OpportunityListPresenterImpl;
import com.tokopedia.seller.opportunity.viewmodel.Opportunity;

import java.util.ArrayList;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nisie on 3/1/17.
 */

public class OpportunityListFragment extends BasePresenterFragment<OpportunityListPresenter>
        implements OpportunityListView {

    private static final int REQUEST_OPEN_DETAIL = 123;
    @BindView(R2.id.opportunity_list)
    RecyclerView opportunityList;

    @BindView(R2.id.header_info)
    TextView headerInfo;

    @BindView(R2.id.search)
    SearchView searchView;

    @BindView(R2.id.fab)
    FloatingActionButton fab;

    OpportunityListAdapter adapter;
    LinearLayoutManager layoutManager;
    RefreshHandler refreshHandler;

    BottomSheetDialog bottomSheetDialog;
    View filterLayout;
    Spinner sortSpinner;
    Spinner shippingTypeSpinner;
    Spinner categorySpinner;
    Button resetButton;
    Button filterButton;

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
        presenter = new OpportunityListPresenterImpl(this,
                new OpportunityNetworkInteractorImpl(),
                new CompositeSubscription()
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
        adapter = OpportunityListAdapter.createInstance(onGoToDetail());
        layoutManager = new LinearLayoutManager(getActivity());
        opportunityList.setLayoutManager(layoutManager);
        opportunityList.setAdapter(adapter);

        refreshHandler = new RefreshHandler(getActivity(), view, onRefresh());
        initBottomSheetDialog(view);
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
            public void goToDetail(Opportunity opportunity) {
//                startActivityForResult(OppurtunityDetailActivity.getDetailIntent(getActivity(), opportunity),
//                        REQUEST_OPEN_DETAIL);
            }
        };
    }

    private void initBottomSheetDialog(View view) {
        filterLayout = getActivity().getLayoutInflater().inflate(R.layout.filter_layout_opportunity, null);
        sortSpinner = (Spinner) filterLayout.findViewById(R.id.sort);
        categorySpinner = (Spinner) filterLayout.findViewById(R.id.category);
        shippingTypeSpinner = (Spinner) filterLayout.findViewById(R.id.shipping_type);
        resetButton = (Button) filterLayout.findViewById(R.id.button_cancel);
        filterButton = (Button) filterLayout.findViewById(R.id.button_confirm);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(filterLayout);
    }

    @Override
    protected void setViewListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });
        opportunityList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                presenter.loadMore(lastItemPosition, visibleItem);
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortSpinner.setSelection(0);
                categorySpinner.setSelection(0);
                shippingTypeSpinner.setSelection(0);
            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.hide();
                presenter.getOpportunity();
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
        if (adapter.getList().size() == 0 || adapter.isEmpty()) {
            adapter.showLoadingFull(true);
        } else {
            adapter.showLoading(true);
        }
    }

    @Override
    public void onSuccessGetOpportunity() {
        finishLoadingList();
        finishRefresh();
        ArrayList<Opportunity> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(new Opportunity("Produk " + i));
        }
        adapter.setList(list);
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
        if (sortSpinner.getSelectedItemPosition() != 0)
            return String.valueOf(sortSpinner.getSelectedItem());
        else return "";
    }

    @Override
    public String getShippingParam() {
        if (shippingTypeSpinner.getSelectedItemPosition() != 0)
            return String.valueOf(shippingTypeSpinner.getSelectedItem());
        else return "";
    }

    @Override
    public String getCategoryParam() {
        if (categorySpinner.getSelectedItemPosition() != 0)
            return String.valueOf(categorySpinner.getSelectedItem());
        else return "";
    }

    private void finishLoadingList() {
        if (adapter.isLoading()) {
            adapter.showLoading(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OPEN_DETAIL && resultCode == Activity.RESULT_OK) {

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
