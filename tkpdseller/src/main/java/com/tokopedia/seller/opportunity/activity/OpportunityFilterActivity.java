package com.tokopedia.seller.opportunity.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.adapter.OpportunityFilterAdapter;
import com.tokopedia.seller.opportunity.adapter.OpportunityShippingAdapter;
import com.tokopedia.seller.opportunity.fragment.OpportunityFilterFragment;
import com.tokopedia.seller.opportunity.fragment.OpportunityShippingFragment;
import com.tokopedia.seller.opportunity.viewmodel.OpportunityFilterActivityViewModel;
import com.tokopedia.seller.opportunity.viewmodel.ShippingTypeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 3/15/17.
 */

public class OpportunityFilterActivity extends BasePresenterActivity
        implements OpportunityFilterAdapter.FilterListener, OpportunityShippingAdapter.ShippingListener {

    public interface FilterListener {
        void updateData(OpportunityFilterActivityViewModel viewModel);
    }

    private static final String ARGS_DATA = "OpportunityFilterActivity_ARGS_DATA";
    private static final String PARAM_FILTER_VIEW_MODEL = "PARAM_FILTER_VIEW_MODEL";
    private List<Fragment> listFragment;
    private OpportunityFilterActivityViewModel viewModel;

    public static Intent createIntent(Context context, OpportunityFilterActivityViewModel data) {
        Intent intent = new Intent(context, OpportunityFilterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARAM_FILTER_VIEW_MODEL, data);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getParcelable(ARGS_DATA) != null) {
            viewModel = savedInstanceState.getParcelable(ARGS_DATA);
        } else if (getIntent().getExtras() != null &&
                getIntent().getExtras().getParcelable(PARAM_FILTER_VIEW_MODEL) != null) {
            viewModel = getIntent().getExtras().getParcelable(PARAM_FILTER_VIEW_MODEL);
        } else {
            viewModel = new OpportunityFilterActivityViewModel();
        }
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_opportunity_filter;

    }

    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        OpportunityFilterFragment fragment = OpportunityFilterFragment.createInstance(viewModel.getListTitle());
        fragmentTransaction.replace(R.id.filter, fragment);

        listFragment = new ArrayList<>();
//        listFragment.add(OpportunityCategoryFragment.createInstance(getIntent().getExtras()));
        listFragment.add(OpportunityShippingFragment.createInstance(viewModel.getListShipping()));
        listFragment.add(OpportunityShippingFragment.createInstance(viewModel.getListShipping()));
        fragmentTransaction.replace(R.id.container, listFragment.get(0));

        fragmentTransaction.commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void onFilterClicked(int pos) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (listFragment.get(pos) instanceof FilterListener)
            ((FilterListener) listFragment.get(pos)).updateData(viewModel);
        fragmentTransaction.replace(R.id.container, listFragment.get(pos));
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ARGS_DATA, viewModel);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onShippingSelected(ShippingTypeViewModel shippingTypeViewModel) {
        viewModel.getListShipping().get(shippingTypeViewModel.getPosition()).setSelected(true);
    }
}