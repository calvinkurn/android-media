package com.tokopedia.seller.topads.presenter;

import android.content.Context;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDashboardShopPresenterImpl extends TopAdsDashboardPresenterImpl {

    private static final int TYPE_SHOP = 2;

    public int getType() {
        return TYPE_SHOP;
    }

    public TopAdsDashboardShopPresenterImpl(Context context) {
        super(context);
    }
}