package com.tokopedia.transaction.purchase.detail.presenter;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.purchase.detail.activity.OrderHistoryView;
import com.tokopedia.transaction.purchase.detail.interactor.OrderHistoryInteractor;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData;

import rx.Subscriber;

/**
 * Created by kris on 11/17/17. Tokopedia
 */

public class OrderHistoryPresenterImpl implements OrderHistoryPresenter {

    private OrderHistoryInteractor orderHistoryInteractor;

    private OrderHistoryView mainView;

    public OrderHistoryPresenterImpl(OrderHistoryInteractor orderHistoryInteractor) {
        this.orderHistoryInteractor = orderHistoryInteractor;
    }

    public void setMainViewListener(OrderHistoryView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void fetchHistoryData(Context context, String orderId) {
        TKPDMapParam<String, String> temporaryParams = new TKPDMapParam<>();
        temporaryParams.put("order_id", orderId);
        temporaryParams.put("user_id", SessionHandler.getLoginID(context));
        temporaryParams.put("lang", "id");
        TKPDMapParam<String, Object> params = new TKPDMapParam<>();
        params.putAll(temporaryParams);
        params.put("request_by", 1);
        orderHistoryInteractor.requestOrderHistoryData(getOrderHistorySubscriber(), params);
    }

    @Override
    public void onDestroy() {
        orderHistoryInteractor.onViewDestroyed();
    }

    private Subscriber<OrderHistoryData> getOrderHistorySubscriber() {
        return new Subscriber<OrderHistoryData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(OrderHistoryData data) {
                mainView.receivedHistoryData(data);
            }
        };
    }
}
