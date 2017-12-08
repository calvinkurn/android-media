package com.tokopedia.inbox.inboxmessageold.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetail;

import java.util.Map;

/**
 * Created by Nisie on 5/11/16.
 */
public interface InboxMessageActRetrofitInteractor {

    void archiveMessage(@NonNull Context context, @NonNull Map<String, String> params,
                        @NonNull ActInboxMessageListener listener);

    void undoArchiveMessage(@NonNull Context context, @NonNull Map<String, String> params,
                            @NonNull ActInboxMessageListener listener);

    void deleteMessage(@NonNull Context context, @NonNull Map<String, String> params,
                       @NonNull ActInboxMessageListener listener);

    void undoDeleteMessage(@NonNull Context context, @NonNull Map<String, String> params,
                           @NonNull ActInboxMessageListener listener);

    void moveToInbox(@NonNull Context context, @NonNull Map<String, String> params,
                     @NonNull ActInboxMessageListener listener);

    void undoMoveToInbox(@NonNull Context context, @NonNull Map<String, String> params,
                         @NonNull ActInboxMessageListener listener);

    void deleteForever(@NonNull Context context, @NonNull Map<String, String> params,
                       @NonNull ActInboxMessageListener listener);

    void flagDetailSpam(@NonNull Context context, @NonNull Map<String, String> params,
                        @NonNull ActInboxMessageListener listener);

    void undoFlagDetailSpam(@NonNull Context context, @NonNull Map<String, String> params,
                            @NonNull ActInboxMessageListener listener);

    void replyMessage(@NonNull Context context, @NonNull Map<String, String> params,
                      @NonNull SendReplyInboxMessageListener listener);

    void markAsRead(@NonNull Context context, @NonNull Map<String, String> params,
                    @NonNull ActInboxMessageListener listener);

    void markAsUnread(@NonNull Context context, @NonNull Map<String, String> params,
                      @NonNull ActInboxMessageListener listener);

    void unSubscribeObservable();

    boolean isRequesting();

    void setRequesting(boolean isRequesting);


    interface ActInboxMessageListener {

        void onSuccess();

        void onTimeout();

        void onError(String error);

        void onNullData();

        void onNoInternetConnection();
    }

    interface SendReplyInboxMessageListener {

        void onSuccess(InboxMessageDetail inboxMessageDetailItem);

        void onTimeout();

        void onError(String error);

        void onNullData();

        void onNoInternetConnection();
    }


}
