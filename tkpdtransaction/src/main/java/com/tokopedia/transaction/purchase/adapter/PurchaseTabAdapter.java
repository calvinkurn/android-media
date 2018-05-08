package com.tokopedia.transaction.purchase.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.transaction.orders.orderlist.view.fragment.OrderListFragment;
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
        Bundle arg = new Bundle();
        Fragment fragment;
        switch (position) {
            case TransactionPurchaseRouter.TAB_POSITION_ORDER_ALL:
                arg.putInt("ordercategory", 0);
                fragment = new OrderListFragment();
                fragment.setArguments(arg);
                return fragment;
            case TransactionPurchaseRouter.TAB_POSITION_ORDER_GOLD:
                arg.putInt("ordercategory", 1);
                fragment = new OrderListFragment();
                fragment.setArguments(arg);
                return fragment;
            case TransactionPurchaseRouter.TAB_POSITION_ORDER_DIGITAL:
                arg.putInt("ordercategory", 2);
                fragment = new OrderListFragment();
                fragment.setArguments(arg);
                return fragment;
            case TransactionPurchaseRouter.TAB_POSITION_ORDER_MARKETPLACE:
                arg.putInt("ordercategory", 3);
                fragment = new OrderListFragment();
                fragment.setArguments(arg);
                return fragment;
            case TransactionPurchaseRouter.TAB_POSITION_ORDER_FLIGHT:
                arg.putInt("ordercategory", 4);
                fragment = new OrderListFragment();
                fragment.setArguments(arg);
                return fragment;
            default:
                arg.putInt("ordercategory", 0);
                fragment = new OrderListFragment();
                fragment.setArguments(arg);
                return fragment;
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
