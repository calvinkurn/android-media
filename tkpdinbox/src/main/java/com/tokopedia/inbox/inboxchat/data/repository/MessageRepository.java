package com.tokopedia.inbox.inboxchat.data.repository;


import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.domain.model.message.MessageData;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.SendMessageViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface MessageRepository {

    Observable<InboxChatViewModel> getMessage(TKPDMapParam<String, Object> requestParams);

    Observable<InboxChatViewModel> deleteMessage(TKPDMapParam<String, Object> parameters);

    Observable<SendMessageViewModel> sendMessage(TKPDMapParam<String, Object> requestParams);
}
