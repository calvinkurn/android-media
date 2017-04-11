package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.activity.OpportunityFilterActivity;
import com.tokopedia.seller.opportunity.adapter.OpportunityShippingAdapter;
import com.tokopedia.seller.opportunity.viewmodel.OpportunityFilterActivityViewModel;
import com.tokopedia.seller.opportunity.viewmodel.ShippingTypeViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisie on 4/7/17.
 */

public class OpportunityShippingFragment extends BasePresenterFragment implements OpportunityFilterActivity.FilterListener {

    private static final String ARGS_SHIPPING_LIST = "ARGS_SHIPPING_LIST";
    RecyclerView shippingList;
    OpportunityShippingAdapter adapter;
    ArrayList<ShippingTypeViewModel> listShipping;

    public OpportunityShippingFragment() {
        this.listShipping = new ArrayList<>();
    }

    public static Fragment createInstance(List<ShippingTypeViewModel> listShipping) {
        OpportunityShippingFragment fragment = new OpportunityShippingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARGS_SHIPPING_LIST, new ArrayList<>(listShipping));
        fragment.setArguments(bundle);
        return fragment;
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
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_opportunity_shipping;
    }

    @Override
    protected void initView(View view) {
        shippingList = (RecyclerView) view.findViewById(R.id.list);
        shippingList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = OpportunityShippingAdapter.createInstance((OpportunityFilterActivity) getActivity());
        shippingList.setAdapter(adapter);
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
    public void onResume() {
        super.onResume();
        if ((listShipping == null || listShipping.size() == 0)
                && getArguments().getParcelableArrayList(ARGS_SHIPPING_LIST) != null)
            listShipping = getArguments().getParcelableArrayList(ARGS_SHIPPING_LIST);
        adapter.setList(listShipping);
    }

    @Override
    public void updateData(OpportunityFilterActivityViewModel viewModel) {
        this.listShipping = new ArrayList<>(viewModel.getListShipping());
    }
}
