package com.tokopedia.inbox.inboxchat.data.repository;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.domain.model.message.MessageData;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface MessageRepository {

    Observable<MessageData> getMessage(TKPDMapParam<String, Object> requestParams);
}
