package com.tokopedia.transaction.orders.orderdetails.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderdetails.di.DaggerOrderDetailsComponent;
import com.tokopedia.transaction.orders.orderdetails.di.OrderDetailsComponent;
import com.tokopedia.transaction.orders.orderlist.common.OrderListContants;
import com.tokopedia.transaction.orders.orderlist.common.SaveDateBottomSheetActivity;
import com.tokopedia.transaction.purchase.detail.fragment.CancelSearchFragment;
import com.tokopedia.transaction.purchase.detail.fragment.RejectOrderBuyerRequest;

import static com.tokopedia.transaction.orders.orderdetails.view.fragment.MarketPlaceDetailFragment.ACTION_BUTTON_URL;
import static com.tokopedia.transaction.orders.orderdetails.view.fragment.MarketPlaceDetailFragment.CANCEL_BUYER_REQUEST;
import static com.tokopedia.transaction.orders.orderdetails.view.fragment.MarketPlaceDetailFragment.REJECT_BUYER_REQUEST;

public class RequestCancelActivity extends BaseSimpleActivity implements HasComponent<OrderDetailsComponent>, RejectOrderBuyerRequest.RejectOrderBuyerRequestListener, CancelSearchFragment.CancelSearchReplacementListener {

    public static final String KEY_ORDER_ID = "OrderId";
    public static final String CANCEL_ORDER_FRAGMENT = "cancelFragment";
    private static final String REJECT_ORDER_FRAGMENT_TAG = "reject_order_fragment_teg";
    String orderID;
    private OrderDetailsComponent orderListComponent;


    public static Intent getInstance(Context context, String orderId, String uri, int cancelorderFragment) {
        Intent intent = new Intent(context, RequestCancelActivity.class);
        intent.putExtra(KEY_ORDER_ID, orderId);
        intent.putExtra(ACTION_BUTTON_URL, uri);
        intent.putExtra(CANCEL_ORDER_FRAGMENT, cancelorderFragment);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderID = getIntent().getStringExtra(KEY_ORDER_ID);
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
        if (getIntent() != null) {
            if (getIntent().getIntExtra(CANCEL_ORDER_FRAGMENT, 1) == 1) {
                return RejectOrderBuyerRequest.createFragment(getIntent().getStringExtra(KEY_ORDER_ID));
            } else {
                return CancelSearchFragment.createFragment(getIntent().getStringExtra(KEY_ORDER_ID));
            }
        } else {
            return null;
        }
    }

    @Override
    public void rejectOrderBuyerRequest(TKPDMapParam<String, String> rejectParam) {
        Intent intent = new Intent();
        intent.putExtra(OrderListContants.REASON,rejectParam.get(OrderListContants.REASON));
        intent.putExtra(OrderListContants.REASON_CODE, 1);
        intent.putExtra(ACTION_BUTTON_URL, getIntent().getStringExtra(ACTION_BUTTON_URL));
        setResult(REJECT_BUYER_REQUEST,intent);
        finish();
    }

    @Override
    public void cancelSearch(String orderId, int reasonId, String notes) {
        Intent intent = new Intent();
        intent.putExtra(OrderListContants.REASON, notes);
        intent.putExtra(OrderListContants.REASON_CODE, reasonId);
        intent.putExtra(ACTION_BUTTON_URL, getIntent().getStringExtra(ACTION_BUTTON_URL));
        setResult(CANCEL_BUYER_REQUEST, intent);
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
