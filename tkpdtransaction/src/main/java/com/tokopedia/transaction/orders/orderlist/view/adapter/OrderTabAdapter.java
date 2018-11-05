package com.tokopedia.transaction.orders.orderlist.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.view.fragment.OrderListFragment;

public class OrderTabAdapter extends FragmentStatePagerAdapter {
    private static final String ORDER_CATEGORY = "orderCategory";
    Listener listener;

    public OrderTabAdapter(FragmentManager fragmentManager, Listener listener) {
        super(fragmentManager);
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return OrderCategory.TABS_CATEGORY.length;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arg = new Bundle();
        String orderCategory = OrderCategory.TABS_CATEGORY[position];
        arg.putString(ORDER_CATEGORY, orderCategory);
        Fragment fragment;
        if(orderCategory.equals(OrderCategory.FLIGHTS)) {
            if(listener.getAppContext() instanceof UnifiedOrderListRouter)
                fragment = ((UnifiedOrderListRouter)listener.getAppContext()).getFlightOrderListFragment();
            else
                fragment = new OrderListFragment();
        } else {
            fragment = new OrderListFragment();
        }
        fragment.setArguments(arg);
        return fragment;

    }

    public interface Listener {
        Context getAppContext();
    }
}
