package com.tokopedia.transaction.orders.orderlist.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tokopedia.flight.orderlist.view.FlightOrderListFragment;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.data.OrderLabelList;
import com.tokopedia.transaction.orders.orderlist.view.fragment.OrderListFragment;

import java.util.List;

public class OrderTabAdapter extends FragmentStatePagerAdapter {
    private static final String ORDER_CATEGORY = "orderCategory";
    Listener listener;
    List<OrderLabelList> adapterItems;

    public OrderTabAdapter(FragmentManager fragmentManager, List<OrderLabelList> adapterItems, Listener listener) {
        super(fragmentManager);
        this.listener = listener;
        this.adapterItems = adapterItems;
    }

    @Override
    public int getCount() {
        return adapterItems.size();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arg = new Bundle();
        String orderCategory = adapterItems.get(position).getOrderCategory();
        arg.putString(ORDER_CATEGORY, orderCategory);
        Fragment fragment;
        if(orderCategory.equals(OrderCategory.FLIGHTS)) {
            fragment = FlightOrderListFragment.createInstance();
        } else {
            fragment = new OrderListFragment();
        }
        fragment.setArguments(arg);
        return fragment;

    }

    public interface Listener {
        String getFilterCaseAllTransaction();
    }
}
