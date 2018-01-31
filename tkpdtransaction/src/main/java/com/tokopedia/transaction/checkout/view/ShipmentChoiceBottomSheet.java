package com.tokopedia.transaction.checkout.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentChoiceAdapter;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;
import com.tokopedia.transaction.checkout.view.presenter.IShipmentChoicePresenter;
import com.tokopedia.transaction.checkout.view.presenter.ShipmentChoicePresenter;
import com.tokopedia.transaction.checkout.view.view.IShipmentChoiceView;

/**
 * Created by Irfan Khoirul on 30/01/18.
 */

public class ShipmentChoiceBottomSheet extends BottomSheetDialog
        implements IShipmentChoiceView, ShipmentChoiceAdapter.ViewListener {

    private RecyclerView rvShipmentChoice;
    private LinearLayout llNetworkErrorView;
    private ProgressBar pbLoading;
    private LinearLayout llShipmentInfoTicker;
    private ImageView imgBtCloseTicker;
    private TextView tvShipmentInfoTicker;
    private ImageButton imgBtClose;

    private ShipmentChoiceAdapter shipmentChoiceAdapter;
    private IShipmentChoicePresenter presenter;
    private ActionListener listener;

    public ShipmentChoiceBottomSheet(@NonNull Context context, ShipmentItemData selectedShipment) {
        super(context);
        initializeView(context);
        initializeData(selectedShipment);
    }

    private void initializeData(ShipmentItemData selectedShipment) {
        presenter = new ShipmentChoicePresenter();
        presenter.attachView(this);
        presenter.loadShipmentChoice(selectedShipment);
        setupRecyclerView();
    }

    public void initializeView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View bottomSheetView = layoutInflater.inflate(R.layout.fragment_shipment_choice, null);
        setContentView(bottomSheetView);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
//        behavior.setPeekHeight(height / 3);

        rvShipmentChoice = bottomSheetView.findViewById(R.id.rv_shipment_choice);
        llNetworkErrorView = bottomSheetView.findViewById(R.id.ll_network_error_view);
        pbLoading = bottomSheetView.findViewById(R.id.pb_loading);
        llShipmentInfoTicker = bottomSheetView.findViewById(R.id.ll_shipment_info_ticker);
        imgBtCloseTicker = bottomSheetView.findViewById(R.id.img_bt_close_ticker);
        tvShipmentInfoTicker = bottomSheetView.findViewById(R.id.tv_shipment_info_ticker);
        imgBtClose = bottomSheetView.findViewById(R.id.img_bt_close);

        imgBtCloseTicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llShipmentInfoTicker.setVisibility(View.GONE);
            }
        });

        imgBtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShipmentChoiceBottomSheet.this.dismiss();
            }
        });

    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    private void setupRecyclerView() {
        shipmentChoiceAdapter = new ShipmentChoiceAdapter(presenter.getShipmentChoices(), this);
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
                        presenter.loadShipmentChoice(null);
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
        if (!TextUtils.isEmpty(shipmentItemData.getShipmentInfo())) {
            tvShipmentInfoTicker.setText(shipmentItemData.getShipmentInfo());
            llShipmentInfoTicker.setVisibility(View.VISIBLE);
        } else {
            llShipmentInfoTicker.setVisibility(View.GONE);
        }
        listener.onShipmentItemClick(shipmentItemData);
        this.dismiss();
    }

    public interface ActionListener {
        void onShipmentItemClick(ShipmentItemData shipmentItemData);
    }
}