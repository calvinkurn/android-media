package com.tokopedia.inbox.contactus.interactor;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.inbox.contactus.model.ContactUsPass;
import com.tokopedia.inbox.contactus.model.solution.SolutionResult;
import com.tokopedia.inbox.inboxticket.interactor.InboxTicketRetrofitInteractor;
import com.tokopedia.inbox.inboxticket.model.replyticket.ReplyResult;

import java.util.Map;

/**
 * Created by nisie on 8/12/16.
 */
public interface ContactUsRetrofitInteractor {

    void sendTicket(@NonNull Context context, @NonNull ContactUsPass params,
                    @NonNull SendTicketListener listener);

    void getSolution(@NonNull Context context, @NonNull String id,
                     @NonNull GetSolutionListener listener);

    void unsubscribe();

    void commentRating(Context context, Map<String, String> commentRatingParam, CommentRatingListener listener);

    public interface SendTicketListener {
        void onSuccess();

        void onNoNetworkConnection();

        void onTimeout(String error);

        void onError(String s);

        void onNullData();

    }

    public interface GetSolutionListener{
        void onSuccess(SolutionResult result);

        void onNoNetworkConnection();

        void onTimeout(String error);

        void onError(String s);

        void onNullData();

    }

    interface CommentRatingListener {

        void onSuccess(ReplyResult replyResult);

        void onTimeout();

        void onError(String error);

        void onNullData();

        void onNoConnectionError();

        void onFailAuth();

    }

}