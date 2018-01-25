package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentChoiceAdapter;
import com.tokopedia.transaction.checkout.view.presenter.IShipmentChoicePresenter;
import com.tokopedia.transaction.checkout.view.view.IShipmentChoiceView;

import butterknife.BindView;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentChoiceFragment extends BasePresenterFragment implements IShipmentChoiceView,
        ShipmentChoiceAdapter.ViewListener {

    @BindView(R2.id.rv_shipment_choice)
    RecyclerView rvShipmentChoice;
    @BindView(R2.id.ll_network_error_view)
    LinearLayout llNetworkErrorView;
    @BindView(R2.id.pb_loading)
    ProgressBar pbLoading;

    private ShipmentChoiceAdapter shipmentChoiceAdapter;
    private IShipmentChoicePresenter presenter;

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
        return R.layout.fragment_shipment_choice;
    }


    @Override
    protected void initView(View view) {
        presenter.loadShipmentChoice();
    }

    @Override
    protected void setViewListener() {
        setupRecyclerView();
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    private void setupRecyclerView() {
        shipmentChoiceAdapter = new ShipmentChoiceAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rvShipmentChoice.setLayoutManager(linearLayoutManager);
        rvShipmentChoice.setAdapter(shipmentChoiceAdapter);
    }

    @Override
    public void showLoading() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onShipmentItemClick() {

    }
}
