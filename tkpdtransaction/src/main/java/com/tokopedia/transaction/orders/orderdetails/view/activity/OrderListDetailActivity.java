package com.tokopedia.transaction.orders.orderdetails.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderdetails.view.OrderListDetailFragment;

import butterknife.BindView;

import static com.tokopedia.transaction.orders.orderdetails.view.OrderListDetailFragment.KEY_ORDER_ID;

/**
 * Created by baghira on 09/05/18.
 */

public class OrderListDetailActivity extends BaseSimpleActivity{

    public static Intent createInstance(Context context, String orderId) {
         Intent intent = new Intent(context, OrderListDetailActivity.class);
         intent.putExtra(KEY_ORDER_ID, orderId);
         Log.e("sandeep","order id at activity ="+orderId);
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
            toolbar.setElevation(0);
        }
        toolbar.setTitleTextAppearance(this, R.style.ToolbarText_SansSerifMedium);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.abstraction.R.color.white));
            final int lFlags = getWindow().getDecorView().getSystemUiVisibility();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(lFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }*/

    }
}
