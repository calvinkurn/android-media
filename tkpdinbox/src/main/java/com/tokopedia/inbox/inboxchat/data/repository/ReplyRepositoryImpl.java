package com.tokopedia.inbox.inboxchat.data.repository;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.data.factory.ReplyFactory;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class ReplyRepositoryImpl implements ReplyRepository{

    private ReplyFactory replyFactory;

    public ReplyRepositoryImpl(ReplyFactory replyFactory){
        this.replyFactory = replyFactory;
    }

    @Override
    public Observable<ChatRoomViewModel> getReply(String id, TKPDMapParam<String, Object> requestParams) {
        return replyFactory.createCloudReplyDataSource().getReply(id, requestParams);
    }

    @Override
    public Observable<ReplyActionData> replyMessage(TKPDMapParam<String, Object> parameters) {
        return replyFactory.createCloudReplyActionDataSource().replyMessage(parameters);
    }
}
