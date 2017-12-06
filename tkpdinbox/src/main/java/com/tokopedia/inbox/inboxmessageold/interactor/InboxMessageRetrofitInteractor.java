package com.tokopedia.inbox.inboxmessageold.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.inbox.inboxmessageold.model.inboxmessage.InboxMessage;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetail;

import java.util.Map;

/**
 * Created by Nisie on 4/29/16.
 */
public interface InboxMessageRetrofitInteractor {

    void getInboxMessage(@NonNull Context context, @NonNull Map<String, String> params,
                         @NonNull GetInboxMessageListener listener);

    void getInboxMessageDetail(@NonNull Context context, @NonNull Map<String, String> params,
                               @NonNull GetInboxMessageDetailListener listener);

    void unSubscribeObservable();

    boolean isRequesting();

    void setRequesting(boolean isRequesting);

    interface GetInboxMessageListener {

        void onSuccess(InboxMessage result);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoConnectionError();
    }

    interface GetInboxMessageDetailListener {

        void onSuccess(InboxMessageDetail result);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoConnectionError();
    }
}
