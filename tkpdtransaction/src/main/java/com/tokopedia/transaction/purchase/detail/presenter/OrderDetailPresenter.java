package com.tokopedia.transaction.purchase.detail.presenter;

import android.content.Context;

import com.tokopedia.transaction.purchase.detail.activity.OrderDetailView;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;

/**
 * Created by kris on 11/10/17. Tokopedia
 */

public interface OrderDetailPresenter {

    void setMainViewListener(OrderDetailView view);

    void fetchData(Context context, String orderId);

    void processInvoice(Context context, OrderDetailData data);

    void processToShop(Context context, OrderDetailData data);

    void processShowComplain(Context context, OrderDetailData data);

    void processComplaint(Context context, OrderDetailData data);

    void processConfirmDeliver(Context context, OrderDetailData data);

    void processTrackOrder(Context context, OrderDetailData data);

    void processSeeAllHistories(Context context, OrderDetailData data);

    void processAskSeller(Context context, OrderDetailData data);

    void processRequestCancelOrder(Context context, OrderDetailData data);

    void processFinish(Context context, String orderId);

    void onDestroyed();

}
