package com.tokopedia.transaction.orders.orderdetails.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.applink.TransactionAppLink;
import com.tokopedia.transaction.orders.orderdetails.di.DaggerOrderDetailsComponent;
import com.tokopedia.transaction.orders.orderdetails.di.OrderDetailsComponent;
import com.tokopedia.transaction.orders.orderdetails.view.fragment.MarketPlaceDetailFragment;
import com.tokopedia.transaction.orders.orderdetails.view.fragment.OmsDetailFragment;
import com.tokopedia.transaction.orders.orderdetails.view.fragment.OrderListDetailFragment;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.purchase.detail.fragment.RejectOrderBuyerRequest;
import com.tokopedia.user.session.UserSession;

/**
 * Created by baghira on 09/05/18.
 */

public class OrderListDetailActivity extends BaseSimpleActivity implements HasComponent<OrderDetailsComponent> {


    private static final String ORDER_ID = "order_id";
    private static final String FROM_PAYMENT = "from_payment";;
    private static final int REQUEST_CODE = 100;
    private String fromPayment = "false";
    private String orderId;
    private OrderDetailsComponent orderListComponent;
    String category = null;


    @DeepLink({TransactionAppLink.ORDER_DETAIL, TransactionAppLink.ORDER_OMS_DETAIL, TransactionAppLink.ORDER_MARKETPLACE_DETAIL})
    public static Intent getOrderDetailIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, OrderListDetailActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @Override
    protected Fragment getNewFragment() {
        if (category != null) {
            category = category.toUpperCase();

            if (category.contains(OrderCategory.DIGITAL)) {
                return OrderListDetailFragment.getInstance(orderId, OrderCategory.DIGITAL);
            } else if (category.contains(OrderCategory.MARKETPLACE)) {
                return MarketPlaceDetailFragment.getInstance(orderId, OrderCategory.MARKETPLACE);
            } else if(category.contains("")) {
                return OmsDetailFragment.getInstance(orderId, "", fromPayment);
            }
        }
        return null;


    }

    @Override
    protected void onCreate(Bundle arg) {
        if (getIntent().getExtras() != null) {
            orderId = getIntent().getStringExtra(ORDER_ID);
            Uri uri = getIntent().getData();
            if (uri != null) {
                fromPayment = uri.getQueryParameter(FROM_PAYMENT);
            }
        }
        UserSession userSession = new UserSession(this);
        if (!userSession.isLoggedIn()) {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE);
        } else {
            category = getIntent().getStringExtra((DeepLink.URI));
        }
        super.onCreate(arg);
        if (fromPayment != null) {
            if (fromPayment.equalsIgnoreCase("true")) {
                updateTitle(getResources().getString(R.string.thank_you));
            }
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                category = getIntent().getStringExtra((DeepLink.URI));

                if (category != null) {
                    category = category.toUpperCase();

                    if (category.contains(OrderCategory.DIGITAL)) {
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.parent_view, OrderListDetailFragment.getInstance(orderId, OrderCategory.DIGITAL)).commit();

                    } else if (category.contains("")) {
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.parent_view, OmsDetailFragment.getInstance(orderId, "", fromPayment)).commit();
                    }
                }
            } else {
                finish();
            }
        }
    }

}
