package com.tokopedia.seller.purchase.detail.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.purchase.detail.adapter.OrderHistoryAdapter;
import com.tokopedia.seller.purchase.detail.customview.OrderHistoryStepperLayout;
import com.tokopedia.seller.purchase.detail.di.DaggerOrderHistoryComponent;
import com.tokopedia.seller.purchase.detail.di.OrderHistoryComponent;
import com.tokopedia.seller.purchase.detail.model.history.viewmodel.OrderHistoryData;
import com.tokopedia.seller.purchase.detail.presenter.OrderHistoryPresenterImpl;

import javax.inject.Inject;

/**
 * Created by kris on 11/7/17. Tokopedia
 */

public class OrderHistoryActivity extends BaseSimpleActivity implements OrderHistoryView {

    private static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";

    private static final String EXTRA_USER_MODE = "EXTRA_USER_MODE";

    private View mainViewContainer;

    private ProgressDialog mainProgressDialog;

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
        mainProgressDialog = new ProgressDialog(this);
        mainViewContainer = findViewById(R.id.main_container);
        initInjector();
        presenter.setMainViewListener(this);
        presenter.fetchHistoryData(this, getExtraOrderId(), getExtraUserMode());
    }

    @Nullable
    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected void inflateFragment() {
        //no op
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.order_history_layout;
    }

    private void initInjector() {
        OrderHistoryComponent component = DaggerOrderHistoryComponent
                .builder()
                .baseAppComponent(((BaseMainApplication) this.getApplication()).getBaseAppComponent())
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
        mainProgressDialog.show();
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

    private String getExtraOrderId() {
        return getIntent().getExtras().getString(EXTRA_ORDER_ID);
    }

    private int getExtraUserMode() {
        return getIntent().getExtras().getInt(EXTRA_USER_MODE);
    }
}
