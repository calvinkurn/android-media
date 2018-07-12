package com.tokopedia.transaction.orders.orderlist.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.applink.TransactionAppLink;
import com.tokopedia.transaction.orders.orderlist.view.fragment.OrderListFragment;

import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION;

public class OrderListActivity extends DrawerPresenterActivity {
    private static final int DIGITAL_CATEGORY = 2;
    private int drawerPosition;

    public static Intent getInstance(Context context){
        return new Intent(context, OrderListActivity.class);
    }
    @DeepLink(TransactionAppLink.ORDER_HISTORY)
    public static Intent getOrderListIntent(Context context, Bundle bundle){
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, OrderListActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.order_list_main, OrderListFragment.newInstance(DIGITAL_CATEGORY), OrderListFragment.class.getSimpleName())
                    .commit();
        }
    }

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
            drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_DIGITAL_TRANSACTION_LIST);
        }
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected int setDrawerPosition() {
        return drawerPosition;
    }
}
