package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.activity.OpportunityFilterActivity;
import com.tokopedia.seller.opportunity.adapter.OpportunityFilterAdapter;
import com.tokopedia.seller.opportunity.viewmodel.FilterViewModel;

/**
 * Created by nisie on 3/15/17.
 */

public class OpportunityFilterFragment extends BasePresenterFragment
        implements OpportunityFilterActivity.FilterListener {


    private static final String ARGS_FILTER_DATA = "ARGS_FILTER_DATA";
    RecyclerView categoryList;
    OpportunityFilterAdapter adapter;
    FilterViewModel filterViewModel;

    public OpportunityFilterFragment() {
    }

    public static Fragment createInstance(FilterViewModel filterViewModel) {
        OpportunityFilterFragment fragment = new OpportunityFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_FILTER_DATA, filterViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_opportunity_category;
    }

    @Override
    protected void initView(View view) {
        categoryList = (RecyclerView) view.findViewById(R.id.list);
        categoryList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = OpportunityFilterAdapter.createInstance((OpportunityFilterActivity) getActivity());
        categoryList.setAdapter(adapter);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

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

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (filterViewModel == null && getArguments().getParcelable(ARGS_FILTER_DATA) != null) {
            filterViewModel = getArguments().getParcelable(ARGS_FILTER_DATA);
        }

        if (adapter != null && filterViewModel != null)
            adapter.setData(filterViewModel);
    }

    @Override
    public void updateData(FilterViewModel viewModel) {
        this.filterViewModel = viewModel;
        if (adapter != null) {
            adapter.setData(viewModel);
            adapter.notifyDataSetChanged();
        }
    }
}
