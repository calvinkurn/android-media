package com.tokopedia.transaction.orders.orderdetails.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderdetails.view.fragment.OrderListDetailFragment;

import static com.tokopedia.transaction.orders.orderdetails.view.fragment.OrderListDetailFragment.KEY_ORDER_ID;

/**
 * Created by baghira on 09/05/18.
 */

public class OrderListDetailActivity extends BaseSimpleActivity{

    public static Intent createInstance(Context context, String orderId) {
         Intent intent = new Intent(context, OrderListDetailActivity.class);
         intent.putExtra(KEY_ORDER_ID, orderId);
        return intent;
    }

    public static Intent createInstance(Context context, String type, String orderId) {
        Intent intent = new Intent(context, OrderListDetailActivity.class);
        intent.putExtra(KEY_ORDER_ID, orderId);
        return intent;
    }
    @Override
    protected Fragment getNewFragment() {
        return OrderListDetailFragment.getInstance((String) getIntent().getExtras().get(KEY_ORDER_ID));
    }
    @Override
    protected void onCreate(Bundle arg) {
        super.onCreate(arg);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.black));
        }
        toolbar.setTitleTextAppearance(this, R.style.ToolbarText_SansSerifMedium);
    }
}
