package com.tokopedia.seller.purchase.detail.presenter;

import android.content.Context;

import com.tokopedia.seller.purchase.detail.activity.OrderHistoryView;
import com.tokopedia.seller.purchase.detail.interactor.OrderHistoryInteractor;
import com.tokopedia.seller.purchase.detail.model.history.viewmodel.OrderHistoryData;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;

import rx.Subscriber;

/**
 * Created by kris on 11/17/17. Tokopedia
 */

public class OrderHistoryPresenterImpl implements OrderHistoryPresenter {

    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";

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
        HashMap<String, String> temporaryParams = new HashMap<>();
        temporaryParams.put("order_id", orderId);
        temporaryParams.put("user_id", userSession.getUserId());
        temporaryParams.put("lang", "id");
        HashMap<String, Object> params = new HashMap<>(temporaryParams);
        params.put("request_by", userMode);
        orderHistoryInteractor.requestOrderHistoryData(getOrderHistorySubscriber(), generateParamsNetwork2(context, params));
    }

    @Override
    public void onDestroy() {
        orderHistoryInteractor.onViewDestroyed();
    }

    private HashMap<String, Object> generateParamsNetwork2(Context context, HashMap<String, Object> params) {
        UserSessionInterface userSession = new UserSession(context);
        String deviceId = userSession.getDeviceId();
        String userId = userSession.getUserId();
        String hash = md5(userId + "~" + deviceId);

        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));

        return params;
    }

    private String md5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b & 0xff));
            }
            return hexString.toString();
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
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
