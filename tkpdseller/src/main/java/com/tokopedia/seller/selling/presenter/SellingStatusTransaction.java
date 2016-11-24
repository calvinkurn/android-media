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

    public abstract void checkValidationToSendGoogleAnalytic(boolean isVisibleToUser, Context context);

    public abstract void getStatusTransactionList(boolean isVisibleToUser, SellingStatusTransactionImpl.Type type);

    public abstract void onQuerySubmit(String query);

    public abstract void onQueryChange(String newText);

    public abstract void onRefreshView();

    public abstract void onScrollList(boolean isLastItemVisible);

    public abstract void refreshOnFilter();

    public abstract void finishConnection();
}
