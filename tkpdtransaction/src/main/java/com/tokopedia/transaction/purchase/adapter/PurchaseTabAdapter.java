package com.tokopedia.transaction.purchase.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.tokopedia.transaction.purchase.fragment.TxListFragment;
import com.tokopedia.transaction.purchase.fragment.TxSummaryFragment;
import com.tokopedia.transaction.purchase.fragment.TxVerificationFragment;

/**
 * PurchaseTabAdapter
 * Created by anggaprasetiyo on 8/26/16.
 */
public class PurchaseTabAdapter extends FragmentStatePagerAdapter {
    public final static int TAB_POSITION_PURCHASE_SUMMARY = 0;
    public final static int TAB_POSITION_PURCHASE_VERIFICATION = 1;
    public final static int TAB_POSITION_PURCHASE_STATUS_ORDER = 2;
    public final static int TAB_POSITION_PURCHASE_DELIVER_ORDER = 3;
    public final static int TAB_POSITION_PURCHASE_ALL_ORDER = 4;

    private final int tabCount;
    private final Listener listener;

    public PurchaseTabAdapter(FragmentManager fm, int tabCount, Listener listener) {
        super(fm);
        this.listener = listener;
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TAB_POSITION_PURCHASE_SUMMARY:
                return TxSummaryFragment.createInstancePurchase();
            case TAB_POSITION_PURCHASE_VERIFICATION:
                return TxVerificationFragment.createInstance();
            case TAB_POSITION_PURCHASE_STATUS_ORDER:
                return TxListFragment.instanceStatusOrder();
            case TAB_POSITION_PURCHASE_DELIVER_ORDER:
                return TxListFragment.instanceDeliverOrder();
            case TAB_POSITION_PURCHASE_ALL_ORDER:
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
}
