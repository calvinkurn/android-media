package com.tokopedia.seller.selling.presenter;

import android.content.Context;

import com.tokopedia.core.session.baseFragment.BaseImpl;
import com.tokopedia.core.util.PagingHandler;

/**
 * Created by Erry on 7/19/2016.
 */
public abstract class SellingStatusTransaction extends BaseImpl<SellingStatusTransactionView> {

    public SellingStatusTransaction(SellingStatusTransactionView view) {
        super(view);
    }

    public abstract void callNetworkTransaction(Context context, String search, String filter, String startDate, String endDate);

    public abstract void callNetworkStatus(Context context, String search);

    public abstract void loadMore(Context context);

    public abstract PagingHandler getPaging();

    public abstract void checkValidationToSendGoogleAnalytic(boolean isVisibleToUser, Context context);
}
