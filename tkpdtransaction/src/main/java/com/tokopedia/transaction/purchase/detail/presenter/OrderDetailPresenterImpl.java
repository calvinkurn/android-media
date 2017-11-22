package com.tokopedia.transaction.purchase.detail.presenter;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
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
        mainView.showMainViewLoadingPage();
        TKPDMapParam<String, String> temporaryHash = new TKPDMapParam<>();
        temporaryHash.put("order_id", orderId);
        temporaryHash.put("user_id", SessionHandler.getLoginID(context));
        temporaryHash.put("lang", "id");
        temporaryHash = AuthUtil.generateParamsNetwork(context, temporaryHash);
        TKPDMapParam<String, Object> params = new TKPDMapParam<>();
        params.putAll(temporaryHash);
        params.put("os_type", 1);
        params.put("request_by", 1);
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
        mainView.onViewComplaint(data.getResoId());
    }

    @Override
    public void processComplaint(Context context, OrderDetailData data) {
        mainView.showComplaintDialog(data.getShopName(), data.getOrderId());
    }

    @Override
    public void processConfirmDeliver(Context context, OrderDetailData data) {
        mainView.showConfirmDialog(data.getOrderId());
    }

    @Override
    public void processTrackOrder(Context context, OrderDetailData data) {
        mainView.trackShipment(data.getOrderId());
    }

    @Override
    public void processSeeAllHistories(Context context, OrderDetailData data) {

    }

    @Override
    public void processAskSeller(Context context, OrderDetailData data) {
        mainView.onAskSeller(data);
    }

    @Override
    public void processRequestCancelOrder(Context context, OrderDetailData data) {

    }

    @Override
    public void processFinish(Context context, String orderId) {
        TKPDMapParam<String, String> orderDetailParams = new TKPDMapParam<>();
        orderDetailParams.put("order_id", orderId);
        orderDetailInteractor.confirmFinishConfirm(confirmShipmentSubscriber(),
                AuthUtil.generateParamsNetwork(context, orderDetailParams));
    }

    @Override
    public void onDestroyed() {
        orderDetailInteractor.onActivityClosed();
    }

    private Subscriber<OrderDetailData> orderDetailDataSubscriber() {
        return new Subscriber<OrderDetailData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mainView.hideMainViewLoadingPage();
                mainView.onError(e.getMessage());
            }

            @Override
            public void onNext(OrderDetailData data) {
                mainView.hideMainViewLoadingPage();
                mainView.onReceiveDetailData(data);
            }
        };
    }

    private Subscriber<String> confirmShipmentSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mainView.onOrderFinished(s);
            }
        };
    }
}
