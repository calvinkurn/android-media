package com.tokopedia.inbox.inboxchat.data.repository;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.ChatSocketData;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface WebSocketRepository {

    Observable<ChatSocketData> listen(TKPDMapParam<String, Object> requestParams);

}
