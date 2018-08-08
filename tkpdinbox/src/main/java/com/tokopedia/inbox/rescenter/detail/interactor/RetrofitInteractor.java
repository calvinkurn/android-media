package com.tokopedia.inbox.rescenter.detail.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.shipping.model.ResCenterKurir;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.ResCenterTrackShipping;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;

import java.util.Map;

/**
 * Created by hangnadi on 2/9/16.
 */
public interface RetrofitInteractor {

    void getResCenterDetail(@NonNull Context context, @NonNull Map<String, String> params,
                            @NonNull ResCenterDetailListener listener);

    void trackShipping(@NonNull Context context, @NonNull Map<String, String> params,
                       @NonNull TrackShippingListener listener);

    void editAddress(@NonNull Context context,
                     @NonNull Map<String, String> params,
                     @NonNull OnPostAddressListener listener);

    interface ResCenterDetailListener {

        void onSuccess(@NonNull DetailResCenterData data);

        void onTimeOut(NetworkErrorHelper.RetryClickedListener listener);

        void onFailAuth();

        void onThrowable(Throwable e);

        void onError(String message);

        void onNullData();
    }

    interface TrackShippingListener {

        void onSuccess(ResCenterTrackShipping resCenterTrackShipping);

        void onTimeOut(String message, NetworkErrorHelper.RetryClickedListener listener);

        void onFailAuth();

        void onThrowable(Throwable e);

        void onError(String message);

        void onNullData();
    }

    void unsubscribe();

    void postAddress(@NonNull Context context, @NonNull Map<String, String> params,
                     @NonNull OnPostAddressListener listener);

    interface OnPostAddressListener {
        void onStart();

        void onSuccess();

        void onTimeOut(NetworkErrorHelper.RetryClickedListener listener);

        void onFailAuth();

        void onError(String message);

    }
}
