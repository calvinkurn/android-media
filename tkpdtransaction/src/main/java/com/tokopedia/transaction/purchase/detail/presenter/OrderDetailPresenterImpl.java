package com.tokopedia.transaction.purchase.detail.presenter;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.purchase.detail.activity.OrderDetailView;
import com.tokopedia.transaction.purchase.detail.interactor.OrderDetailInteractor;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;

import rx.Subscriber;

/**
 * Created by kris on 11/10/17. Tokopedia
 */

public class OrderDetailPresenterImpl implements OrderDetailPresenter {

    private OrderDetailView mainView;

    private OrderDetailInteractor orderDetailInteractor;

    public OrderDetailPresenterImpl(OrderDetailInteractor orderDetailInteractor) {
        this.orderDetailInteractor = orderDetailInteractor;
    }

    @Override
    public void setMainViewListener(OrderDetailView view) {
        mainView = view;
    }

    @Override
    public void fetchData(Context context, String orderId) {
        TKPDMapParam<String, Object> params = new TKPDMapParam<>();
        params.put("order_id", orderId);
        params.put("user_id", SessionHandler.getLoginID(context));
        params.put("request_by", 1);
        params.put("lang", "id");
        params.put("os_type", 1);
        orderDetailInteractor.requestDetailData(orderDetailDataSubscriber(), params);
    }

    @Override
    public void processInvoice(Context context, OrderDetailData data) {
        AppUtils.InvoiceDialog(
                context, data.getInvoiceUrl(),
                data.getInvoiceNumber()
        );
    }

    @Override
    public void processToShop(Context context, OrderDetailData data) {

    }

    @Override
    public void processShowComplain(Context context, OrderDetailData data) {

    }

    @Override
    public void processConfirmDeliver(Context context, OrderDetailData data) {

    }

    @Override
    public void processTrackOrder(Context context, OrderDetailData data) {

    }

    @Override
    public void processSeeAllHistories(Context context, OrderDetailData data) {

    }

    @Override
    public void processAskSeller(Context context, OrderDetailData data) {

    }

    @Override
    public void processRequestCancelOrder(Context context, OrderDetailData data) {

    }

    @Override
    public void processComplain(Context context, OrderDetailData data) {

    }

    @Override
    public void processFinish(Context context, OrderDetailData data) {

    }

    private Subscriber<OrderDetailData> orderDetailDataSubscriber() {
        return new Subscriber<OrderDetailData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(OrderDetailData data) {
                mainView.onReceiveDetailData(data);
            }
        };
    }
}
