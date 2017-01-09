package com.tokopedia.transaction.purchase.presenter;

import android.app.Activity;

import com.tokopedia.transaction.purchase.model.response.txverification.TxVerData;

/**
 * @author Angga.Prasetiyo on 13/06/2016.
 */
public interface TxVerDetailPresenter {

    void getTxInvoiceData(String paymentId);

    int getTypePaymentMethod(TxVerData txVerData);

    void onDestroyView();

    void uploadProofImageWSV4(Activity activity, String imagePath, TxVerData txVerData);

}
