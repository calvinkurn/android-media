package com.tokopedia.seller.topads.presenter;

import android.content.Context;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDashboardProductPresenterImpl extends TopAdsDashboardPresenterImpl {

    private static final int TYPE_PRODUCT = 1;

    public int getType() {
        return TYPE_PRODUCT;
    }

    public TopAdsDashboardProductPresenterImpl(Context context) {
        super(context);
    }
}