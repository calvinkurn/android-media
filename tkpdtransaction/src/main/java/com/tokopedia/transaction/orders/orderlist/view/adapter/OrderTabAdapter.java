package com.tokopedia.transaction.orders.orderlist.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.data.OrderLabelList;
import com.tokopedia.transaction.orders.orderlist.view.fragment.OrderListFragment;

import java.util.List;

public class OrderTabAdapter extends FragmentStatePagerAdapter {
    private static final String ORDER_CATEGORY = OrderCategory.KEY_LABEL;
    private static final String ORDER_TAB_LIST = "TAB_LIST";
    Listener listener;
    List<OrderLabelList> mOrderLabelList;

    public OrderTabAdapter(FragmentManager fragmentManager, Listener listener, List<OrderLabelList> orderLabelList) {
        super(fragmentManager);
        this.listener = listener;
        this.mOrderLabelList = orderLabelList;
    }

    @Override
    public int getCount() {
        return mOrderLabelList.size();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arg = listener.getBundle();
        String orderCategory = mOrderLabelList.get(position).getOrderCategory();
        OrderLabelList orderLabelList = mOrderLabelList.get(position);
        arg.putParcelable(ORDER_TAB_LIST, orderLabelList);
        arg.putString(ORDER_CATEGORY, orderCategory);
        Fragment fragment = null;
        if(orderCategory.equals(OrderCategory.FLIGHTS) && listener.getAppContext() instanceof UnifiedOrderListRouter) {
            fragment = ((UnifiedOrderListRouter) listener.getAppContext()).getFlightOrderListFragment();
        }
        if (fragment == null) {
            fragment = new OrderListFragment();
        }
        fragment.setArguments(arg);
        return fragment;

    }

    public interface Listener {
        Context getAppContext();
        Bundle getBundle();
    }
}
