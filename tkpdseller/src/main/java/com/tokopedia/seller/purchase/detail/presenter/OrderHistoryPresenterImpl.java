package com.tokopedia.seller.purchase.detail.presenter;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.purchase.detail.activity.OrderHistoryView;
import com.tokopedia.seller.purchase.detail.interactor.OrderHistoryInteractor;
import com.tokopedia.seller.purchase.detail.model.history.viewmodel.OrderHistoryData;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

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
    public void fetchHistoryData(Context context, String orderId, int userMode) {
        mainView.showMainViewLoadingPage();
        UserSessionInterface userSession = new UserSession(context);
        TKPDMapParam<String, String> temporaryParams = new TKPDMapParam<>();
        temporaryParams.put("order_id", orderId);
        temporaryParams.put("user_id", userSession.getUserId());
        temporaryParams.put("lang", "id");
        TKPDMapParam<String, Object> params = new TKPDMapParam<>();
        params.putAll(temporaryParams);
        params.put("request_by", userMode);
        orderHistoryInteractor.requestOrderHistoryData(getOrderHistorySubscriber(),
                AuthUtil.generateParamsNetwork2(context, params));
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
                mainView.hideMainViewLoadingPage();
                mainView.onLoadError(e.getMessage());
            }

            @Override
            public void onNext(OrderHistoryData data) {
                mainView.hideMainViewLoadingPage();
                mainView.receivedHistoryData(data);
            }
        };
    }
}
