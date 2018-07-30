package com.tokopedia.transaction.purchase.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.applink.TransactionAppLink;
import com.tokopedia.transaction.purchase.adapter.PurchaseTabAdapter;
import com.tokopedia.transaction.purchase.dialog.CancelTransactionDialog;
import com.tokopedia.transaction.purchase.fragment.TxListFragment;
import com.tokopedia.transaction.purchase.fragment.TxSummaryFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_ALL_ORDER;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_CONFIRMED;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_DELIVERED;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_DELIVER_ORDER;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_PROCESSED;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_SHIPPED;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_VERIFICATION;


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

    @DeepLink(TransactionAppLink.ORDER_LIST)
    public static Intent getIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, PurchaseActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    @DeepLink(TransactionAppLink.PURCHASE_VERIFICATION)
    public static Intent getCallingIntentPurchaseVerification(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, PurchaseActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_PURCHASE_VERIFICATION)
                .putExtras(extras);
    }

    @DeepLink("tokopedia://buyer/confirmed")
    public static Intent getConfirmedIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, PurchaseActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_PURCHASE_CONFIRMED)
                .putExtras(extras);
    }

    @DeepLink("tokopedia://buyer/processed")
    public static Intent getProcessedIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, PurchaseActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_PURCHASE_PROCESSED)
                .putExtras(extras);
    }

    @DeepLink("tokopedia://buyer/shipped")
    public static Intent getShippedIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, PurchaseActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_PURCHASE_SHIPPED)
                .putExtras(extras);
    }

    @DeepLink("tokopedia://buyer/delivered")
    public static Intent getDeliveredIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, PurchaseActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_PURCHASE_DELIVERED)
                .putExtras(extras);
    }

    @DeepLink(TransactionAppLink.PURCHASE_ORDER)
    public static Intent getCallingIntentPurchaseStatus(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, PurchaseActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_PURCHASE_STATUS_ORDER)
                .putExtras(extras);
    }

    @DeepLink(TransactionAppLink.PURCHASE_SHIPPING_CONFIRM)
    public static Intent getCallingIntentPurchaseShipping(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, PurchaseActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_PURCHASE_DELIVER_ORDER)
                .putExtras(extras);
    }

    @DeepLink(TransactionAppLink.PURCHASE_HISTORY)
    public static Intent getCallingIntentPurchaseHistory(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, PurchaseActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_PURCHASE_ALL_ORDER)
                .putExtras(extras);
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
    protected int getLayoutId() {
        if (GlobalConfig.isSellerApp())
            return R.layout.activity_purchase_tx_module;
        return R.layout.layout_tablayout_secondary;
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
        tabContents.add(TransactionPurchaseRouter.TAB_POSITION_PURCHASE_SUMMARY,
                getString(R.string.title_tab_purchase_summary));
        tabContents.add(TAB_POSITION_PURCHASE_CONFIRMED, getString(R.string.label_tx_confirmed));
        tabContents.add(TAB_POSITION_PURCHASE_PROCESSED, getString(R.string.label_tx_processed));
        tabContents.add(TAB_POSITION_PURCHASE_SHIPPED, getString(R.string.label_tx_shipped));
        tabContents.add(TAB_POSITION_PURCHASE_DELIVERED, getString(R.string.label_tx_delivered));
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
            startActivity(HomeRouter.getHomeActivity(this));
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

}
