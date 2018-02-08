package com.tokopedia.transaction.purchase.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.transaction.purchase.activity.TxDetailActivity;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderButton;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderData;
import com.tokopedia.transaction.purchase.model.response.txlist.OrderShop;

/**
 * @author Angga.Prasetiyo on 28/04/2016.
 */
public interface TxDetailPresenter {
    void processInvoice(TxDetailActivity txDetailActivity, OrderData data);

    void processToShop(Context context, OrderShop orderShop);

    void processShowComplain(Context context, OrderButton orderButton, OrderShop orderShop);

    void processOpenDispute(Context context, OrderData orderData, int state);

    void processConfirmDeliver(Context context, OrderData orderData);

    void processTrackOrder(Context context, OrderData orderData);

    void processSeeAllHistories(Context context, OrderData orderData);

    void processAskSeller(Context context, OrderData orderData);

    void onActivityResult(Context context, int requestCode, int resultCode, Intent data);

    void onDestroyView();

    void processRequestCancelOrder(Activity activity, String reason, OrderData orderData);

    void processComplain(Context context, OrderData orderData);

    void processFinish(Context context, OrderData orderData);
}
