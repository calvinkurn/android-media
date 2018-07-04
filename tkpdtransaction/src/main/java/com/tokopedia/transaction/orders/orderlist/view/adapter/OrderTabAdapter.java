package com.tokopedia.transaction.orders.orderlist.view.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.tokopedia.transaction.orders.orderlist.view.fragment.OrderListFragment;

import java.util.List;

public class OrderTabAdapter extends FragmentStatePagerAdapter {
    private static final String ORDER_CATEGORY = "orderCategory";
    Listener listener;
    List<String> adapterItems;

    public OrderTabAdapter(FragmentManager fragmentManager, List<String> adapterItems, Listener listener) {
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
        arg.putString(ORDER_CATEGORY, adapterItems.get(position));
        Fragment fragment = new OrderListFragment();
        Log.e("sandeep","fragment created");
        fragment.setArguments(arg);
        return fragment;

    }

    public interface Listener {
        String getFilterCaseAllTransaction();
    }
}
