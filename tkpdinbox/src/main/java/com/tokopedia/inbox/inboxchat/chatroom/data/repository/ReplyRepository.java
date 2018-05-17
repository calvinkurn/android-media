package com.tokopedia.inbox.inboxchat.chatroom.data.repository;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.ChatRoomViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface ReplyRepository {

    Observable<ChatRoomViewModel> getReply(String id, TKPDMapParam<String, Object> requestParams);

    Observable<ReplyActionData> replyMessage(TKPDMapParam<String, Object> parameters);
}
