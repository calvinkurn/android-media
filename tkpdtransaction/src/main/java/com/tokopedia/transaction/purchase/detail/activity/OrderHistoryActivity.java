package com.tokopedia.transaction.purchase.detail.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
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

    private static final String EXTRA_USER_MODE = "EXTRA_USER_MODE";

    private View mainViewContainer;

    private TkpdProgressDialog mainProgressDialog;

    @Inject
    OrderHistoryPresenterImpl presenter;

    public static Intent createInstance(Context context, String orderId, int userMode) {
        Intent intent = new Intent(context, OrderHistoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ORDER_ID, orderId);
        bundle.putInt(EXTRA_USER_MODE, userMode);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.order_history_layout);
        mainProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS);
        mainViewContainer = findViewById(R.id.main_container);
        initInjector();
        presenter.setMainViewListener(this);
        presenter.fetchHistoryData(this, getExtraOrderId(), getExtraUserMode());
    }

    private void initInjector() {
        OrderHistoryComponent component = DaggerOrderHistoryComponent
                .builder()
                .appComponent(getApplicationComponent())
                .build();
        component.inject(this);
    }

    private void initView(OrderHistoryData data) {
        OrderHistoryStepperLayout stepperLayout = findViewById(R.id.order_history_stepper_layout);
        stepperLayout.setStepperStatus(data);

        RecyclerView orderHistoryList = findViewById(R.id.order_history_list);

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
        NetworkErrorHelper.showEmptyState(this, getMainView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.fetchHistoryData(OrderHistoryActivity.this,
                        getExtraOrderId(),
                        getExtraUserMode());
            }
        });
    }

    private View getMainView() {
        return findViewById(R.id.main_view);
    }

    @Override
    public void showMainViewLoadingPage() {
        mainProgressDialog.showDialog();
        mainViewContainer.setVisibility(View.GONE);
    }

    @Override
    public void hideMainViewLoadingPage() {
        mainProgressDialog.dismiss();
        mainViewContainer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    private String getExtraOrderId() {
        return getIntent().getExtras().getString(EXTRA_ORDER_ID);
    }

    private int getExtraUserMode() {
        return getIntent().getExtras().getInt(EXTRA_USER_MODE);
    }
}
