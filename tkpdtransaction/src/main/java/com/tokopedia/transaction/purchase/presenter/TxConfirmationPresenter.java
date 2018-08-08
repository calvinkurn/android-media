package com.tokopedia.transaction.purchase.presenter;

import android.content.Context;

import com.tokopedia.transaction.purchase.model.response.txconfirmation.TxConfData;

import java.util.Set;

/**
 * @author Angga.Prasetiyo on 13/05/2016.
 */
public interface TxConfirmationPresenter {
    void getPaymentConfirmationData(Context context, int page, int typeRequest);

    void processToTxConfirmationDetail(Context context, TxConfData data);

    void processMultiConfirmPayment(Context context, Set<TxConfData> datas);

    void processMultipleCancelPayment(Context context, Set<TxConfData> datas);

    void onDestroyView();

}
