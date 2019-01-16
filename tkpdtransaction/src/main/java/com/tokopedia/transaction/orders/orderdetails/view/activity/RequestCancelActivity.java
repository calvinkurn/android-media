package com.tokopedia.transaction.orders.orderdetails.view.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderdetails.di.DaggerOrderDetailsComponent;
import com.tokopedia.transaction.orders.orderdetails.di.OrderDetailsComponent;
import com.tokopedia.transaction.orders.orderdetails.view.fragment.MarketPlaceDetailFragment;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.purchase.detail.fragment.CancelSearchFragment;
import com.tokopedia.transaction.purchase.detail.fragment.RejectOrderBuyerRequest;
import com.tokopedia.transaction.purchase.detail.presenter.OrderDetailPresenterImpl;

import javax.inject.Inject;

public class RequestCancelActivity extends BaseSimpleActivity implements HasComponent<OrderDetailsComponent>, RejectOrderBuyerRequest.RejectOrderBuyerRequestListener, CancelSearchFragment.CancelSearchReplacementListener {

    public static final String KEY_ORDER_ID = "OrderId";
    private static final String REJECT_ORDER_FRAGMENT_TAG = "reject_order_fragment_teg";
    String orderID;
    MarketPlaceDetailFragment marketPlaceDetailFragment;
    private OrderDetailsComponent orderListComponent;
    int fragmentToShow = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderID = getIntent().getStringExtra(KEY_ORDER_ID);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            toolbar.setElevation(10);
//            toolbar.setBackgroundResource(com.tokopedia.core2.R.color.white);
//        } else {
//            toolbar.setBackgroundResource(com.tokopedia.core2.R.drawable.bg_white_toolbar_drop_shadow);
//        }
    }

    @Override
    public OrderDetailsComponent getComponent() {
        if (orderListComponent == null) initInjector();
        return orderListComponent;
    }

    private void initInjector() {
        orderListComponent = DaggerOrderDetailsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
        GraphqlClient.init(this);
    }

    @Override
    protected android.support.v4.app.Fragment getNewFragment() {
        return null;
    }

    protected void inflateFragment() {
        Fragment newFragment = getSimpleFragment();
        if (newFragment == null) {
            return;
        }

        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.enter_bottom, R.animator.enter_bottom)
                .replace(com.tokopedia.abstraction.R.id.parent_view, newFragment, REJECT_ORDER_FRAGMENT_TAG)
                .commit();
    }

    protected Fragment getSimpleFragment() {
        if (getIntent().getIntExtra("cancelFragment", 1) == 1) {
            return RejectOrderBuyerRequest.createFragment(getIntent().getStringExtra(KEY_ORDER_ID));
        } else {
            return CancelSearchFragment.createFragment(getIntent().getStringExtra(KEY_ORDER_ID));
        }

    }

    @Override
    public void rejectOrderBuyerRequest(TKPDMapParam<String, String> rejectParam) {
        Intent i = new Intent();
        i.putExtra("reason",rejectParam.get("reason"));
        i.putExtra("r_code", 1);
        setResult(1,i);
        finish();
    }

    @Override
    public void cancelSearch(String orderId, int reasonId, String notes) {
        Intent intent = new Intent();
        intent.putExtra("reason", notes);
        intent.putExtra("reason_code", reasonId);
        intent.putExtra("r_code", reasonId);
        setResult(2, intent);
        finish();
    }

    @Override
    public void setToolbarCancelSearch(String titleToolbar, int drawable) {
        toolbar.setTitle(titleToolbar);
        toolbar.setNavigationIcon(drawable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_out_right, R.animator.slide_out_right)
                .remove(getFragmentManager()
                        .findFragmentByTag(REJECT_ORDER_FRAGMENT_TAG)).commit();
    }
}
