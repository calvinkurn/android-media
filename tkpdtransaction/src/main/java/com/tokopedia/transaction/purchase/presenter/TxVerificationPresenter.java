package com.tokopedia.transaction.purchase.presenter;

import android.content.Context;

import com.tokopedia.transaction.purchase.model.response.txverification.TxVerData;

/**
 * @author Angga.Prasetiyo on 24/05/2016.
 */
public interface TxVerificationPresenter {
    void getPaymentVerification(Context context, int page, int typeRequest);

    void processEditPayment(Context context, TxVerData data);

    void processToTxVerificationDetail(Context context, TxVerData data);

    void processCancelTransaction(Context context, TxVerData data);

    void confirmCancelTransaction(Context context, String paymentId);

    void uploadProofImageWSV4(Context context, String imagePath, TxVerData txVerData);

    void onDestroyView();

}
