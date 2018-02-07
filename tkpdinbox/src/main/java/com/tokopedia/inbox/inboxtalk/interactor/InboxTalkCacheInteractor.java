package com.tokopedia.inbox.inboxtalk.interactor;

import org.json.JSONObject;

/**
 * Created by stevenfredian on 5/17/16.
 */
public interface InboxTalkCacheInteractor {

    void storeFirstPage(String nav, String filter, JSONObject result);

    void getInboxTalkFromCache(String nav, GetInboxTalkListener getInboxTalkListener);

    interface GetInboxTalkListener{
        void onError(String error);

        void onThrowable(Throwable e);

        void onTimeout();

        void onSuccess(JSONObject result);
    }

    void unsubscribe();
}
