package com.tokopedia.inbox.rescenter.shipping.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.shipping.model.ShippingParamsPostModel;
import com.tokopedia.inbox.rescenter.shipping.model.ResCenterKurir;

import rx.Observable;

/**
 * Created by hangnadi on 12/14/16.
 */
public interface RetrofitInteractor {

    void getShippingList(@NonNull Context context,
                         @NonNull TKPDMapParam params,
                         @NonNull GetKurirListener listener);

    void unSubscribe();

    interface GetKurirListener {

        void onStart();

        void onSuccess(ResCenterKurir shippingCourier);

        void onTimeOut(NetworkErrorHelper.RetryClickedListener listener);

        void onError(String message);

    }

    interface PostShippingListener {

        void onStart();

        void onSuccess();

        void onTimeOut();

        void onError(String message);

    }
}
