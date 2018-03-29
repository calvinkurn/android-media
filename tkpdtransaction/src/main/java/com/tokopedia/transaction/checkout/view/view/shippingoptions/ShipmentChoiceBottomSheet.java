package com.tokopedia.transaction.checkout.view.view.shippingoptions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.shipmentrates.ShipmentItemData;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentChoiceAdapter;
import com.tokopedia.transaction.checkout.view.di.component.DaggerShipmentChoiceComponent;
import com.tokopedia.transaction.checkout.view.di.component.ShipmentChoiceComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Irfan Khoirul on 30/01/18.
 */

public class ShipmentChoiceBottomSheet extends BottomSheetDialog
        implements IShipmentChoiceView, ShipmentChoiceAdapter.ViewListener {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
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
    @BindView(R2.id.img_bt_close)
    ImageButton imgBtClose;

    private ActionListener listener;
    private View bottomSheetView;

    @Inject
    ShipmentChoiceAdapter shipmentChoiceAdapter;

    @Inject
    IShipmentChoicePresenter presenter;

    public ShipmentChoiceBottomSheet(@NonNull Context context,
                                     @NonNull ShipmentDetailData shipmentDetailData,
                                     @Nullable ShipmentItemData selectedShipment) {
        super(context);
        initializeView(context);
        initializeData(shipmentDetailData, selectedShipment);
    }

    private void initializeView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        bottomSheetView = layoutInflater.inflate(R.layout.fragment_shipment_choice, null);
        setContentView(bottomSheetView);
        ButterKnife.bind(this, bottomSheetView);
        initializeInjector();
    }

    private void initializeData(ShipmentDetailData shipmentDetailData, ShipmentItemData selectedShipment) {
        presenter.attachView(this);
        presenter.loadShipmentChoice(shipmentDetailData, selectedShipment);
        setupRecyclerView();
    }

    private void initializeInjector() {
        ShipmentChoiceComponent shipmentChoiceComponent = DaggerShipmentChoiceComponent.builder()
                .build();
        shipmentChoiceComponent.inject(this);
    }

    public void updateHeight() {
        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        int bottomSheetHeight = rvShipmentChoice.computeVerticalScrollRange() +
                toolbar.getHeight() + layoutParams.bottomMargin;
        behavior.setPeekHeight(bottomSheetHeight);
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    private void setupRecyclerView() {
        shipmentChoiceAdapter.setShipments(presenter.getShipmentChoices());
        shipmentChoiceAdapter.setViewListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
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
        NetworkErrorHelper.showEmptyState(getContext(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.loadShipmentChoice(null, null);
                    }
                });
    }

    @Override
    public void showData() {
        if (!TextUtils.isEmpty(presenter.getShipmentDetailData().getShipmentTickerInfo())) {
            tvShipmentInfoTicker.setText(presenter.getShipmentDetailData().getShipmentTickerInfo());
            llShipmentInfoTicker.setVisibility(View.VISIBLE);
        } else {
            llShipmentInfoTicker.setVisibility(View.GONE);
        }

        if (shipmentChoiceAdapter != null) {
            shipmentChoiceAdapter.notifyDataSetChanged();
            rvShipmentChoice.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onShipmentItemClick(ShipmentItemData shipmentItemData) {
        listener.onShipmentItemClick(shipmentItemData);
        this.dismiss();
    }

    @OnClick(R2.id.img_bt_close_ticker)
    void onCloseTickerClick() {
        llShipmentInfoTicker.setVisibility(View.GONE);
    }

    @OnClick(R2.id.img_bt_close)
    void onCloseClick() {
        ShipmentChoiceBottomSheet.this.dismiss();
    }

    public interface ActionListener {
        void onShipmentItemClick(ShipmentItemData shipmentItemData);
    }
}