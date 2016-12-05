package com.tokopedia.transaction.purchase.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.inputmethod.InputMethodManager;

import com.tokopedia.core.GCMListenerService;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.router.TransactionRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.cart.activity.CartActivity;
import com.tokopedia.transaction.purchase.adapter.PurchaseTabAdapter;
import com.tokopedia.transaction.purchase.fragment.TxListFragment;
import com.tokopedia.transaction.purchase.fragment.TxSummaryFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * @author by anggaprasetiyo on 8/26/16.
 */
public class PurchaseActivity extends DrawerPresenterActivity implements
        TxSummaryFragment.OnCenterMenuClickListener, GCMListenerService.NotificationListener,
        PurchaseTabAdapter.Listener, TxListFragment.StateFilterListener {

    @BindView(R2.id.pager)
    ViewPager viewPager;
    @BindView(R2.id.indicator)
    TabLayout indicator;

    public List<String> tabContents;

    private int drawerPosition;
    private String stateTxFilterID;

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
        drawerPosition = extras.getInt(TransactionPurchaseRouter.EXTRA_STATE_TAB_POSITION,
                PurchaseTabAdapter.TAB_POSITION_PURCHASE_SUMMARY);
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
        PurchaseTabAdapter adapter = new PurchaseTabAdapter(
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
        tabContents.add(PurchaseTabAdapter.TAB_POSITION_PURCHASE_SUMMARY,
                getString(R.string.title_dashboard_purchase));
        tabContents.add(PurchaseTabAdapter.TAB_POSITION_PURCHASE_VERIFICATION,
                getString(R.string.title_payment_verification));
        tabContents.add(PurchaseTabAdapter.TAB_POSITION_PURCHASE_STATUS_ORDER,
                getString(R.string.title_order_status));
        tabContents.add(PurchaseTabAdapter.TAB_POSITION_PURCHASE_DELIVER_ORDER,
                getString(R.string.title_receive_confirmation));
        tabContents.add(PurchaseTabAdapter.TAB_POSITION_PURCHASE_ALL_ORDER,
                getString(R.string.title_transaction_list));
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        if (!SessionHandler.isV4Login(getBaseContext())) {
            finish();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void setActionVar() {
        String screenName = getString(R.string.transaction_people_page);
        ScreenTracking.screenLoca(screenName);
    }

    private void setDrawerSidePosition(int position) {
        switch (position) {
            case PurchaseTabAdapter.TAB_POSITION_PURCHASE_SUMMARY:
                drawer.setDrawerPosition(TkpdState.DrawerPosition.PEOPLE_TRANSACTION);
                break;
            case PurchaseTabAdapter.TAB_POSITION_PURCHASE_VERIFICATION:
                drawer.setDrawerPosition(TkpdState.DrawerPosition.PEOPLE_CONFIRM_PAYMENT);
                break;
            case PurchaseTabAdapter.TAB_POSITION_PURCHASE_STATUS_ORDER:
                drawer.setDrawerPosition(TkpdState.DrawerPosition.PEOPLE_ORDER_STATUS);
                break;
            case PurchaseTabAdapter.TAB_POSITION_PURCHASE_DELIVER_ORDER:
                drawer.setDrawerPosition(TkpdState.DrawerPosition.PEOPLE_CONFIRM_SHIPPING);
                break;
            case PurchaseTabAdapter.TAB_POSITION_PURCHASE_ALL_ORDER:
                switch (stateTxFilterID) {
                    case TransactionPurchaseRouter.TRANSACTION_CANCELED_FILTER_ID:
                        drawer.setDrawerPosition(
                                TkpdState.DrawerPosition.PEOPLE_TRANSACTION_CANCELED
                        );
                        break;
                    default:
                        drawer.setDrawerPosition(
                                TkpdState.DrawerPosition.PEOPLE_TRANSACTION_LIST
                        );
                        break;
                }
                break;
            default:
                drawer.setDrawerPosition(TkpdState.DrawerPosition.PEOPLE_TRANSACTION);
                break;
        }
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

    @Override
    protected void initView() {
        super.initView();
        if (getIntent().getExtras() != null && getIntent().getExtras()
                .getBoolean(TransactionPurchaseRouter.EXTRA_UPDATE_BALANCE, false))
            drawer.updateBalance();
    }

    private class OnTabPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {

        OnTabPageChangeListener(TabLayout tabLayout) {
            super(tabLayout);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            hideKeyboard();
            setDrawerSidePosition(position);
        }

        private void hideKeyboard() {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
        }
    }
}
