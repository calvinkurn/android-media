package com.tokopedia.inbox.inboxmessageold.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Created by Nisie on 5/26/16.
 */
public interface SendMessageRetrofitInteractor {

    void sendMessage(@NonNull Context context, @NonNull Map<String, String> params,
                     @NonNull SendMessageListener listener);

    interface SendMessageListener {

        void onSuccess();

        void onTimeout();

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }

    void unSubscribeObservable();


}
