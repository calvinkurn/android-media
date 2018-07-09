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
import com.tokopedia.transaction.orders.orderlist.view.activity.OrderListActivity;

import static com.tokopedia.transaction.orders.orderdetails.view.fragment.OrderListDetailFragment.KEY_ORDER_CATEGORY;
import static com.tokopedia.transaction.orders.orderdetails.view.fragment.OrderListDetailFragment.KEY_ORDER_ID;

/**
 * Created by baghira on 09/05/18.
 */

public class OrderListDetailActivity extends BaseSimpleActivity {

    private String fromPayment = "false";
    private String orderCategory = "";
    private String orderId;


    @DeepLink({TransactionAppLink.ORDER_DETAIL, TransactionAppLink.ORDER_OMS_DETAIL, TransactionAppLink.ORDER_DETAIL_PAYMENT})
    public static Intent getOrderDetailIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, OrderListDetailActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

//    @DeepLink(TransactionAppLink.ORDER_OMS_DETAIL)
//    public static Intent getOrderOMSDetailIntent(Context context, Bundle bundle) {
//        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
//        return new Intent(context, OrderListDetailActivity.class)
//                .setData(uri.build())
//                .putExtras(bundle);
//    }

    public static Intent createInstance(Context context, String orderId, String orderCategory, boolean fromPayment) {
        Intent intent = new Intent(context, OrderListDetailActivity.class);
        intent.putExtra(KEY_ORDER_ID, orderId);
        intent.putExtra(KEY_ORDER_CATEGORY, orderCategory);
        intent.putExtra("fromPayment", fromPayment);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        String category = getIntent().getStringExtra((DeepLink.URI));
        if (category != null) {
            category = category.toUpperCase();

            if (category.contains("DIGITAL")) {
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
            orderCategory = getIntent().getStringExtra("order_category");
            orderId = getIntent().getStringExtra("order_id");
            fromPayment = getIntent().getStringExtra("from_payment");
        }
        super.onCreate(arg);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.black));
        }
        toolbar.setTitleTextAppearance(this, R.style.ToolbarText_SansSerifMedium);
        if (fromPayment != null) {
            if (fromPayment.equalsIgnoreCase("true")) {
                toolbar.setTitle("Thank You");
            }
        }

    }
}
