package com.tokopedia.transaction.purchase.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.tokopedia.transaction.purchase.fragment.TxConfirmationFragment;
import com.tokopedia.transaction.purchase.fragment.TxListFragment;
import com.tokopedia.transaction.purchase.fragment.TxSummaryFragment;
import com.tokopedia.transaction.purchase.fragment.TxVerificationFragment;

/**
 * PurchaseTabAdapter
 * Created by anggaprasetiyo on 8/26/16.
 */
public class PurchaseTabAdapter extends FragmentStatePagerAdapter {
    private final static int TAB_COUNT = 6;
    private final Listener listener;

    public PurchaseTabAdapter(FragmentManager fm, Listener listener) {
        super(fm);
        this.listener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return TxConfirmationFragment.createInstance();
            case 2:
                return TxVerificationFragment.createInstance();
            case 3:
                return TxListFragment.instanceStatusOrder();
            case 4:
                return TxListFragment.instanceDeliverOrder();
            case 5:
                return TxListFragment.instanceAllOrder(listener.getFilterCaseAllTransaction());
            default:
                return TxSummaryFragment.createInstancePurchase();
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    public interface Listener {
        String getFilterCaseAllTransaction();
    }
}
