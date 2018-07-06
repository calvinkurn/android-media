package com.tokopedia.transaction.orders.orderlist.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.tokopedia.flight.orderlist.view.FlightOrderListFragment;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.data.OrderLabelList;
import com.tokopedia.transaction.orders.orderlist.view.fragment.OrderListFragment;

import java.util.List;

public class OrderTabAdapter extends FragmentStatePagerAdapter {
    private static final String ORDER_CATEGORY = "orderCategory";
    Listener listener;
    List<OrderLabelList> adapterItems;
    private String orderCategory;

    public OrderTabAdapter(FragmentManager fragmentManager, List<OrderLabelList> adapterItems, Listener listener, String orderCategory) {
        super(fragmentManager);
        this.listener = listener;
        this.adapterItems = adapterItems;
        this.orderCategory = orderCategory;
    }

    @Override
    public int getCount() {
        return adapterItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arg = new Bundle();
        Fragment fragment = new OrderListFragment();
        arg.putString(ORDER_CATEGORY, adapterItems.get(position).getOrderCategory());
        if(orderCategory.equals(OrderCategory.FLIGHTS)) {
           fragment = FlightOrderListFragment.createInstance();
        }
        fragment.setArguments(arg);
        return fragment;

    }

    public interface Listener {
        String getFilterCaseAllTransaction();
    }
}
