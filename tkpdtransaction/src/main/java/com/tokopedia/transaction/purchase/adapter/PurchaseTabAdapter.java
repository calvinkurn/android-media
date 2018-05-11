package com.tokopedia.transaction.purchase.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.transaction.purchase.fragment.TxListFragment;
import com.tokopedia.transaction.purchase.fragment.TxSummaryFragment;
import com.tokopedia.transaction.purchase.fragment.TxVerificationFragment;

/**
 * @author anggaprasetiyo on 8/26/16.
 */
public class PurchaseTabAdapter extends FragmentStatePagerAdapter {

    private final int tabCount;
    private final Listener listener;
    private TxVerificationFragment verificationFragment;

    public PurchaseTabAdapter(FragmentManager fm, int tabCount, Listener listener) {
        super(fm);
        this.listener = listener;
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TransactionPurchaseRouter.TAB_POSITION_PURCHASE_SUMMARY:
                return TxSummaryFragment.createInstancePurchase();
            case TransactionPurchaseRouter.TAB_POSITION_PURCHASE_VERIFICATION:
                return getVerificationFragment();
            case TransactionPurchaseRouter.TAB_POSITION_PURCHASE_STATUS_ORDER:
                return TxListFragment.instanceStatusOrder();
            case TransactionPurchaseRouter.TAB_POSITION_PURCHASE_DELIVER_ORDER:
                return TxListFragment.instanceDeliverOrder();
            case TransactionPurchaseRouter.TAB_POSITION_PURCHASE_ALL_ORDER:
                return TxListFragment.instanceAllOrder(listener.getFilterCaseAllTransaction());
            default:
                return TxSummaryFragment.createInstancePurchase();
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public interface Listener {
        String getFilterCaseAllTransaction();
    }

    public TxVerificationFragment getVerificationFragment() {
        if(verificationFragment != null) {
            return verificationFragment;
        } else return verificationFragment = TxVerificationFragment.createInstance();
    }
}
