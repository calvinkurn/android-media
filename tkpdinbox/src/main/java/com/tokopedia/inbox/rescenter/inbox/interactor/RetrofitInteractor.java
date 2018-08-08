package com.tokopedia.inbox.rescenter.inbox.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.inbox.model.ResCenterInboxData;

import java.util.Map;

/**
 * Created on 4/6/16.
 */
public interface RetrofitInteractor {

    void getResCenterInbox(@NonNull Context context, @NonNull Map<String, String> params,
                          @NonNull ResCenterInboxListener listener);

    void unsubscribe();

    interface ResCenterInboxListener {

        void onPreExecute(Map<String, String> params);

        void onSuccess(Map<String, String> params, ResCenterInboxData data);

        void onTimeout(NetworkErrorHelper.RetryClickedListener listener);

        void onError(String message);

        void onNullData();

        void onComplete();
    }
}
