package com.tokopedia.transaction.orders.orderlist.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.applink.TransactionAppLink;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.view.fragment.OrderListFragment;

import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.EXTRA_OMS_ORDER_CATEGORY;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION;

public class OrderListActivity extends DrawerPresenterActivity {
    private static final String ORDER_CATEGORY = "orderCategory";
    private int drawerPosition;
    private String orderCategory;


    @DeepLink(TransactionAppLink.ORDER_HISTORY)
    public static Intent getOrderListIntent(Context context, Bundle bundle){
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, OrderListActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        drawerPosition = extras.getInt(EXTRA_STATE_TAB_POSITION,
                TransactionPurchaseRouter.TAB_POSITION_PURCHASE_SUMMARY);
        if (extras.getString(EXTRA_OMS_ORDER_CATEGORY).equals(OrderCategory.DEALS)) {
            orderCategory = OrderCategory.DEALS;
        } else {
            orderCategory = OrderCategory.DIGITAL;
        }
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_list_module;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(drawerHelper != null) {
            if (orderCategory.equals(OrderCategory.DIGITAL)) {
                drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_DIGITAL_TRANSACTION_LIST);
            } else {
                drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_OMS_TRANSACTION_LIST);
            }
        }
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        Fragment fragment;
        if (getFragmentManager().findFragmentByTag(OrderListFragment.class.getSimpleName()) == null) {
            fragment = new OrderListFragment();
        } else {
            fragment = getFragmentManager().findFragmentByTag(OrderListFragment.class.getSimpleName());
        }
        Bundle arg = new Bundle();
        if (orderCategory.equals(OrderCategory.DIGITAL)) {
            arg.putString(ORDER_CATEGORY, OrderCategory.DIGITAL);
        } else if (orderCategory.equals(OrderCategory.DEALS)){
            arg.putString(ORDER_CATEGORY, OrderCategory.DEALS);
        }
        fragment.setArguments(arg);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.order_list_main, fragment, OrderListFragment.class.getSimpleName())
                .commit();
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected int setDrawerPosition() {
        return drawerPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.black));
//        }
////        toolbar.setTitleTextAppearance(this, R.style.ToolbarText_SansSerifMedium);
//        if (orderCategory.equals("DIGITAL")) {
//            toolbar.setTitle("DIGITAL");
//        } else {
//            toolbar.setTitle("Entertainment");
//        }
    }
}
