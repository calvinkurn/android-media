package com.tokopedia.transaction.cart.presenter;

import android.app.Activity;

import com.tokopedia.transaction.cart.model.cartdata.TransactionList;


/**
 * @author anggaprasetiyo on 11/3/16.
 */

public interface ICartPresenter {
    void processGetCartData(Activity activity);

    void processCancelCart(Activity activity, TransactionList data);
}
