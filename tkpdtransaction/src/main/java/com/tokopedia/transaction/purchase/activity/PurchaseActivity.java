package com.tokopedia.transaction.purchase.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.inputmethod.InputMethodManager;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
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
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_DELIVER_ORDER;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER;
import static com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter.TAB_POSITION_PURCHASE_VERIFICATION;


/**
 * @author by anggaprasetiyo on 8/26/16.
 */
public class PurchaseActivity extends DrawerPresenterActivity implements
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

    @DeepLink(TransactionAppLink.PURCHASE_VERIFICATION)
    public static Intent getCallingIntentPurchaseVerification(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, PurchaseActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_PURCHASE_VERIFICATION)
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
        return R.layout.activity_purchase_tx_module;
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
        tabContents.add(TAB_POSITION_PURCHASE_VERIFICATION,
                getString(R.string.title_tab_purchase_status_payment));
        tabContents.add(TAB_POSITION_PURCHASE_STATUS_ORDER,
                getString(R.string.title_tab_purchase_status_order));
        tabContents.add(TAB_POSITION_PURCHASE_DELIVER_ORDER,
                getString(R.string.title_tab_purchase_confirm_deliver));
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
            switch (position) {
                case TransactionPurchaseRouter.TAB_POSITION_PURCHASE_SUMMARY:
                    drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_TRANSACTION);
                    break;
                case TAB_POSITION_PURCHASE_VERIFICATION:
                    drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_PAYMENT_STATUS);
                    break;
                case TAB_POSITION_PURCHASE_STATUS_ORDER:
                    drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_ORDER_STATUS);
                    break;
                case TAB_POSITION_PURCHASE_DELIVER_ORDER:
                    drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_CONFIRM_SHIPPING);
                    break;
                case TAB_POSITION_PURCHASE_ALL_ORDER:
                    switch (stateTxFilterID) {
                        case TransactionPurchaseRouter.TRANSACTION_CANCELED_FILTER_ID:
                            drawerHelper.setSelectedPosition(
                                    TkpdState.DrawerPosition.PEOPLE_TRANSACTION_CANCELED
                            );
                            break;
                        default:
                            drawerHelper.setSelectedPosition(
                                    TkpdState.DrawerPosition.PEOPLE_TRANSACTION_LIST
                            );
                            break;
                    }
                    break;
                default:
                    drawerHelper.setSelectedPosition(TkpdState.DrawerPosition.PEOPLE_TRANSACTION);
                    break;
            }
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
