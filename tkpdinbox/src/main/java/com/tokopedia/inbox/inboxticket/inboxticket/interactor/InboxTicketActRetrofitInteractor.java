package com.tokopedia.inbox.inboxticket.inboxticket.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.inbox.inboxticket.inboxticket.model.giverating.GiveRating;

import java.util.Map;

/**
 * Created by Nisie on 4/20/16.
 */
public interface InboxTicketActRetrofitInteractor {

    void setRating(@NonNull Context context, @NonNull Map<String, String> params,
                        @NonNull SetRatingInboxTicketListener listener);

    interface SetRatingInboxTicketListener {

        void onSuccess(GiveRating result);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoInternetConnection();
    }
}
