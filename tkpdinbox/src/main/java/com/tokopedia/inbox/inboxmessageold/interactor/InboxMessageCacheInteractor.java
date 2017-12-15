package com.tokopedia.inbox.inboxmessageold.interactor;

import com.tokopedia.inbox.inboxmessageold.model.inboxmessage.InboxMessage;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetail;

/**
 * Created by Nisie on 5/10/16.
 */
public interface InboxMessageCacheInteractor {
    void getInboxMessageCache(String nav, GetInboxMessageCacheListener listener);

    void setInboxMessageCache(String nav, InboxMessage inboxMessage);

    void getInboxMessageDetailCache(String MessageId, GetInboxMessageDetailCacheListener listener);

    void setInboxMessageDetailCache(String MessageId, InboxMessageDetail response);

    void deleteCache();

    interface GetInboxMessageCacheListener {
        void onSuccess(InboxMessage inboxMessage);

        void onError(Throwable e);
    }

    interface GetInboxMessageDetailCacheListener {
        void onSuccess(InboxMessageDetail inboxMessage);

        void onError(Throwable e);
    }

}
