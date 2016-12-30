package com.tokopedia.transaction.purchase.presenter;

import android.content.Context;

import com.tokopedia.transaction.purchase.model.response.txconfirmation.TxConfData;

/**
 * @author Angga.Prasetiyo on 15/06/2016.
 */
public interface TxConfDetailPresenter {

    void processCancelTransaction(Context context, TxConfData txConfData);

    void processConfirmTransaction(Context context, TxConfData txConfData);

    void onDestroyView();

}
