package com.tokopedia.transaction.purchase.detail.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.adapter.OrderHistoryAdapter;
import com.tokopedia.transaction.purchase.detail.customview.OrderHistoryStepperLayout;
import com.tokopedia.transaction.purchase.detail.di.DaggerOrderHistoryComponent;
import com.tokopedia.transaction.purchase.detail.di.OrderHistoryComponent;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData;
import com.tokopedia.transaction.purchase.detail.presenter.OrderHistoryPresenterImpl;

import javax.inject.Inject;

/**
 * Created by kris on 11/7/17. Tokopedia
 */

public class OrderHistoryActivity extends TActivity implements OrderHistoryView {

    private static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";

    @Inject
    OrderHistoryPresenterImpl presenter;

    public static Intent createInstance(Context context, String orderId) {
        Intent intent = new Intent(context, OrderHistoryActivity.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.order_history_layout);
        initInjector();
        presenter.setMainViewListener(this);
        presenter.fetchHistoryData(this, getExtraOrderId());
    }

    private void initInjector() {
        OrderHistoryComponent component = DaggerOrderHistoryComponent
                .builder()
                .appComponent(getApplicationComponent())
                .build();
        component.inject(this);
    }

    private void initView(OrderHistoryData data) {
        OrderHistoryStepperLayout stepperLayout = (OrderHistoryStepperLayout)
                findViewById(R.id.order_history_stepper_layout);
        stepperLayout.setStepperStatus(data);

        RecyclerView orderHistoryList = (RecyclerView) findViewById(R.id.order_history_list);
        orderHistoryList.setNestedScrollingEnabled(false);
        orderHistoryList.setLayoutManager(new LinearLayoutManager(this));
        orderHistoryList.setAdapter(new OrderHistoryAdapter(
                data.getOrderListData()));
    }

    @Override
    public void receivedHistoryData(OrderHistoryData data) {
        initView(data);
    }

    @Override
    public void onLoadError(String message) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private String getExtraOrderId() {
        return getIntent().getStringExtra(EXTRA_ORDER_ID);
    }
}
