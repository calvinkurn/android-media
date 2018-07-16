package com.tokopedia.transaction.orders.orderdetails.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.applink.TransactionAppLink;
import com.tokopedia.transaction.orders.orderdetails.view.fragment.OmsDetailFragment;
import com.tokopedia.transaction.orders.orderdetails.view.fragment.OrderListDetailFragment;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;

/**
 * Created by baghira on 09/05/18.
 */

public class OrderListDetailActivity extends BaseSimpleActivity {

    private static final String ORDER_ID = "order_id";
    private static final String FROM_PAYMENT = "from_payment";
    private String fromPayment = "false";
    private String orderId;

    @DeepLink({TransactionAppLink.ORDER_DETAIL, TransactionAppLink.ORDER_OMS_DETAIL})
    public static Intent getOrderDetailIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, OrderListDetailActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @Override
    protected Fragment getNewFragment() {
        String category = getIntent().getStringExtra((DeepLink.URI));
        if (category != null) {
            category = category.toUpperCase();

            if (category.contains(OrderCategory.DIGITAL)) {
                return OrderListDetailFragment.getInstance(orderId, OrderCategory.DIGITAL);
            } else if (category.contains("")) {
                return OmsDetailFragment.getInstance(orderId, "", fromPayment);
            }
        }
        finish();
        return null;


    }

    @Override
    protected void onCreate(Bundle arg) {
        if (getIntent().getExtras() != null) {
            orderId = getIntent().getStringExtra(ORDER_ID);
            Uri uri = getIntent().getData();
            if(uri != null){
                fromPayment = uri.getQueryParameter(FROM_PAYMENT);
            }
        }
        super.onCreate(arg);
        if (fromPayment != null) {
            if (fromPayment.equalsIgnoreCase("true")) {
                updateTitle(getResources().getString(R.string.thank_you));
            }
        }

    }
}
