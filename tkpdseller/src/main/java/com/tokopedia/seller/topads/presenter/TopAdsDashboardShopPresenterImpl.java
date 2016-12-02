package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.seller.topads.view.listener.TopAdsDashboardFragmentListener;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardProductFragmentListener;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardStoreFragmentListener;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDashboardShopPresenterImpl extends TopAdsDashboardPresenterImpl {

    private static final int TYPE_SHOP = 2;

    private TopAdsDashboardStoreFragmentListener topAdsDashboardFragmentListener;

    public void setTopAdsDashboardFragmentListener(TopAdsDashboardStoreFragmentListener topAdsDashboardFragmentListener) {
        this.topAdsDashboardFragmentListener = topAdsDashboardFragmentListener;
    }

    @Override
    public TopAdsDashboardFragmentListener getDashboardListener() {
        return topAdsDashboardFragmentListener;
    }

    @Override
    public int getType() {
        return TYPE_SHOP;
    }

    public TopAdsDashboardShopPresenterImpl(Context context) {
        super(context);
    }
}