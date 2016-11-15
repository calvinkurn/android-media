package com.tokopedia.inbox.inboxticket.inboxticket.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.inbox.inboxticket.inboxticket.model.InboxTicketParam;
import com.tokopedia.inbox.inboxticket.inboxticket.model.inboxticket.InboxTicket;
import com.tokopedia.inbox.inboxticket.inboxticket.model.inboxticketdetail.InboxTicketDetail;
import com.tokopedia.inbox.inboxticket.inboxticket.model.inboxticketdetail.TicketReply;
import com.tokopedia.inbox.inboxticket.inboxticket.model.replyticket.ReplyResult;

import java.util.Map;

/**
 * Created by Nisie on 4/19/16.
 */
public interface InboxTicketRetrofitInteractor {

    void getInboxTicket(@NonNull Context context, @NonNull Map<String, String> params,
                          @NonNull GetInboxTicketListener listener);

    void getInboxTicketDetail(@NonNull Context context, @NonNull Map<String, String> params,
                        @NonNull GetInboxTicketDetailListener listener);

    void viewMore(@NonNull Context context, @NonNull Map<String, String> params,
                              @NonNull ViewMoreListener listener);

    void sendReply(@NonNull Context context, @NonNull InboxTicketParam params,
                  @NonNull ReplyTicketListener listener);

    boolean isRequesting();

    void setRequesting(boolean isRequesting);

    void unsubscribe();

    interface GetInboxTicketListener {

        void onSuccess(@NonNull InboxTicket result);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoConnectionError();

    }

    interface GetInboxTicketDetailListener {

        void onSuccess(@NonNull InboxTicketDetail result);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoConnectionError();

    }

    interface ViewMoreListener {

        void onSuccess(@NonNull TicketReply result);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoConnectionError();

    }

    interface ReplyTicketListener {

        void onSuccess(ReplyResult replyResult);

        void onTimeout();

        void onError(String error);

        void onNullData();

        void onNoConnectionError();

        void onFailAuth();


    }

}
