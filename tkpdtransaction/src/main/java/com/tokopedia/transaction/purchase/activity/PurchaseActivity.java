package com.tokopedia.transaction.purchase.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.inputmethod.InputMethodManager;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.presentation.BaseTemporaryDrawerActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.view.activity.OrderListActivity;
import com.tokopedia.transaction.purchase.adapter.PurchaseTabAdapter;
import com.tokopedia.transaction.purchase.dialog.CancelTransactionDialog;
import com.tokopedia.transaction.purchase.fragment.TxListFragment;
import com.tokopedia.transaction.purchase.fragment.TxSummaryFragment;
import com.tokopedia.transaction.router.ITransactionOrderDetailRouter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_ALL_ORDER;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_CONFIRMED;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_DELIVERED;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_PROCESSED;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_SHIPPED;

/**
 * @author by anggaprasetiyo on 8/26/16.
 */
public class PurchaseActivity extends BaseTemporaryDrawerActivity implements
        TxSummaryFragment.OnCenterMenuClickListener, NotificationReceivedListener,
        PurchaseTabAdapter.Listener, TxListFragment.StateFilterListener,
        CancelTransactionDialog.CancelPaymentDialogListener {

    @BindView(R2.id.pager)
    ViewPager viewPager;
    @BindView(R2.id.indicator)
    TabLayout indicator;

    public List<String> tabContents;

    private int drawerPosition;
    private String stateTxFilterID;
    private PurchaseTabAdapter adapter;

    public static Intent newInstance(Context context) {
        return new Intent(context, PurchaseActivity.class);
    }

    @DeepLink(ApplinkConst.ORDER_LIST)
    public static Intent getIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, PurchaseActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    @DeepLink({ApplinkConst.PURCHASE_CONFIRMED, ApplinkConst.PURCHASE_ORDER})
    public static Intent getConfirmedIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        if (!openNewBom(context)) {
            return new Intent(context, PurchaseActivity.class)
                    .setData(uri.build())
                    .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_PURCHASE_CONFIRMED)
                    .putExtras(extras);
        } else {
            return getMarketPlaceIntent(context, extras);
        }
    }

    @DeepLink(ApplinkConst.PURCHASE_PROCESSED)
    public static Intent getProcessedIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        if (!openNewBom(context)) {
            return new Intent(context, PurchaseActivity.class)
                    .setData(uri.build())
                    .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_PURCHASE_PROCESSED)
                    .putExtras(extras);
        } else {
            return getMarketPlaceIntent(context, extras);
        }
    }

    @DeepLink({ApplinkConst.PURCHASE_SHIPPED})
    public static Intent getShippedIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        if (!openNewBom(context)) {
            return new Intent(context, PurchaseActivity.class)
                    .setData(uri.build())
                    .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_PURCHASE_SHIPPED)
                    .putExtras(extras);
        } else {
            return getMarketPlaceIntent(context, extras);
        }
    }

    @DeepLink({ApplinkConst.PURCHASE_DELIVERED, ApplinkConst.PURCHASE_SHIPPING_CONFIRM})
    public static Intent getDeliveredIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        if (!openNewBom(context)) {
            return new Intent(context, PurchaseActivity.class)
                    .setData(uri.build())
                    .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_PURCHASE_DELIVERED)
                    .putExtras(extras);
        } else {
            return getMarketPlaceIntent(context, extras);
        }
    }

    @DeepLink(ApplinkConst.PURCHASE_HISTORY)
    public static Intent getHistoryIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        if (!openNewBom(context)) {
            return new Intent(context, PurchaseActivity.class)
                    .setData(uri.build())
                    .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_PURCHASE_ALL_ORDER)
                    .putExtras(extras);
        } else {
            return getMarketPlaceIntent(context, extras);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationModHandler.clearCacheIfFromNotification(this, getIntent());
    }

    @Override
    protected int setDrawerPosition() {
        return drawerPosition;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_TX_PEOPLE_TRANSACTION_BUYING_LIST;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        drawerPosition = extras.getInt(EXTRA_STATE_TAB_POSITION,
                TransactionPurchaseRouter.TAB_POSITION_PURCHASE_SUMMARY);
        stateTxFilterID = extras.getString(TransactionPurchaseRouter.EXTRA_STATE_TX_FILTER,
                TransactionPurchaseRouter.ALL_STATUS_FILTER_ID);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return false;
    }

    @Override
    protected int getContentId() {
        if (GlobalConfig.isSellerApp())
            return super.getContentId();
        return R.layout.layout_tab_secondary;
    }

    @Override
    protected int getLayoutId() {
        if (GlobalConfig.isSellerApp())
            return R.layout.activity_purchase_tx_module;
        return super.getLayoutId();
    }

    @Override
    protected void setViewListener() {
        for (String tabContent : tabContents)
            indicator.addTab(indicator.newTab().setText(tabContent));
        adapter = new PurchaseTabAdapter(
                getFragmentManager(), tabContents.size(), this
        );
        viewPager.setOffscreenPageLimit(tabContents.size());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new OnTabPageChangeListener(indicator));
        indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(viewPager));
        viewPager.setCurrentItem(drawerPosition);
    }

    @Override
    protected void initVar() {
        tabContents = new ArrayList<>();
        tabContents.add(TAB_POSITION_PURCHASE_CONFIRMED, getString(R.string.tkpdtransaction_label_tx_confirmed));
        tabContents.add(TAB_POSITION_PURCHASE_PROCESSED, getString(R.string.tkpdtransaction_label_tx_processed));
        tabContents.add(TAB_POSITION_PURCHASE_SHIPPED, getString(R.string.tkpdtransaction_label_tx_shipped));
        tabContents.add(TAB_POSITION_PURCHASE_DELIVERED, getString(R.string.tkpdtransaction_label_tx_delivered));
        tabContents.add(TAB_POSITION_PURCHASE_ALL_ORDER,
                getString(R.string.title_tab_purchase_transactions));
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        if (!SessionHandler.isV4Login(getBaseContext())) {
            finish();
        }

        setDrawerSidePosition(drawerPosition);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(Constants.EXTRA_APPLINK_FROM_PUSH, false)) {
            startActivity(HomeRouter.getHomeActivityInterfaceRouter(this));
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void OnMenuClick(int position, String stateTxFilter) {
        this.stateTxFilterID = stateTxFilter;
        this.viewPager.setCurrentItem(position);
    }

    @Override
    public String getFilterCaseAllTransaction() {
        return stateTxFilterID;
    }

    @Override
    public String getStateTxFilterID() {
        return stateTxFilterID;
    }

    private void setDrawerSidePosition(int position) {
        if (drawerHelper != null) {
            drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_SHOPPING_LIST);
        }
    }

    @Override
    public void confirmCancelDialog(String paymentId) {
        adapter.getVerificationFragment().confirmCancelPayment(paymentId);
    }

    private class OnTabPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {

        OnTabPageChangeListener(TabLayout tabLayout) {
            super(tabLayout);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            hideKeyboard();
            drawerPosition = position;
            setDrawerSidePosition(drawerPosition);
        }

        private void hideKeyboard() {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
        }
    }

    private static boolean openNewBom(Context context) {
        return (((ITransactionOrderDetailRouter) context.getApplicationContext()).getBooleanRemoteConfig(RemoteConfigKey.APP_GLOBAL_NAV_NEW_DESIGN, true));
    }

    private static Intent getMarketPlaceIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        extras.putString("orderCategory", OrderCategory.MARKETPLACE);
        extras.putString(TransactionPurchaseRouter.EXTRA_STATE_MARKETPLACE_FILTER, extras.getString(TransactionPurchaseRouter.EXTRA_STATE_MARKETPLACE_FILTER));
        Intent intent = new Intent(context, OrderListActivity.class);
        intent.setData(uri.build());
        return intent.putExtras(extras);
    }

}
