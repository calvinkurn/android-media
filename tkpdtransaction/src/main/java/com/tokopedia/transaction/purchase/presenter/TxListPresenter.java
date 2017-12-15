package com.tokopedia.transaction.purchase.presenter;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.transaction.purchase.model.AllTxFilter;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderData;

/**
 * @author Angga.Prasetiyo on 21/04/2016.
 */
public interface TxListPresenter {

    void getStatusOrderData(Context context, int page, int typeRequest);

    void getDeliverOrderData(Context context, int page, int typeRequest);

    void getAllOrderData(Context context, int page, AllTxFilter filter, int typeRequest);

    void processToDetailOrder(Context context, OrderData data, int typeInstance);

    void processToInvoice(Context context, OrderData data);

    void processRejectOrder(Context context, OrderData data);

    void processConfirmDeliver(Context context, OrderData data, int typeInstance);

    void processOpenDispute(Context activity, OrderData data, int state);

    void processTrackOrder(Context context, OrderData data);

    void processShowComplain(Context context, OrderData data);

    void onActivityResult(Context context, int requestCode, int resultCode, Intent data);

    void onDestroyView();

    void cancelReplacement(Context context, OrderData orderData);

    void processComplain(Context context, OrderData orderData);

    void processComplainConfirmDeliver(Context context, OrderData orderData);
}
