package com.tokopedia.transaction.checkout.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentChoiceAdapter;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;
import com.tokopedia.transaction.checkout.view.presenter.IShipmentChoicePresenter;
import com.tokopedia.transaction.checkout.view.presenter.ShipmentChoicePresenter;
import com.tokopedia.transaction.checkout.view.view.IShipmentChoiceView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentChoiceFragment extends BasePresenterFragment<IShipmentChoicePresenter>
        implements IShipmentChoiceView, ShipmentChoiceAdapter.ViewListener {

    public static final String EXTRA_SHIPMENT_ITEM_DATA = "shipmentItemData";

    @BindView(R2.id.rv_shipment_choice)
    RecyclerView rvShipmentChoice;
    @BindView(R2.id.ll_network_error_view)
    LinearLayout llNetworkErrorView;
    @BindView(R2.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R2.id.ll_shipment_info_ticker)
    LinearLayout llShipmentInfoTicker;
    @BindView(R2.id.img_bt_close_ticker)
    ImageView imgBtCloseTicker;
    @BindView(R2.id.tv_shipment_info_ticker)
    TextView tvShipmentInfoTicker;

    private ShipmentChoiceAdapter shipmentChoiceAdapter;
    private IShipmentChoicePresenter presenter;

    public static ShipmentChoiceFragment newInstance() {
        ShipmentChoiceFragment fragment = new ShipmentChoiceFragment();
        Bundle bundle = new Bundle();
        // Todo : Add bundle if any
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
        presenter = new ShipmentChoicePresenter();
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
        ButterKnife.bind(view);
        presenter.attachView(this);
//        presenter.loadShipmentChoice();
        setupRecyclerView();
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

    private void setupRecyclerView() {
        shipmentChoiceAdapter = new ShipmentChoiceAdapter(presenter.getShipmentChoices(), this);
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
    public void showNoConnection(String message) {
        rvShipmentChoice.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
//                        presenter.loadShipmentChoice();
                    }
                });
    }

    @Override
    public void showData() {
        if (shipmentChoiceAdapter != null) {
            shipmentChoiceAdapter.notifyDataSetChanged();
            rvShipmentChoice.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onShipmentItemClick(ShipmentItemData shipmentItemData) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SHIPMENT_ITEM_DATA, shipmentItemData);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @OnClick(R2.id.img_bt_close_ticker)
    void onCloseTicker() {
        llShipmentInfoTicker.setVisibility(View.GONE);
    }
}
