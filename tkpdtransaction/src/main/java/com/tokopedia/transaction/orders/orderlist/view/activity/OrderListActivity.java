package com.tokopedia.transaction.orders.orderlist.view.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderlist.view.fragment.OrderListFragment;

import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION;

public class OrderListActivity extends DrawerPresenterActivity {
    private int drawerPosition;
    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        drawerPosition = extras.getInt(EXTRA_STATE_TAB_POSITION,
                TransactionPurchaseRouter.TAB_POSITION_PURCHASE_SUMMARY);
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
        if(drawerHelper != null){
            drawerHelper.setSelectedPosition(drawerPosition);
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
        arg.putInt("ordercategory", 2);
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
}
