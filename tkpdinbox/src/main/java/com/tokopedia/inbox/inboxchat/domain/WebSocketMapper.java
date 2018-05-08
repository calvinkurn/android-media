package com.tokopedia.inbox.inboxchat.domain;

import com.google.gson.GsonBuilder;
import com.tokopedia.inbox.inboxchat.domain.model.reply.FallbackAttachment;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.Message;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 5/8/18.
 */
public class WebSocketMapper {

    private static final String TYPE_QUICK_REPLY = "8";

    public WebSocketMapper() {
    }


    public BaseChatViewModel map(String json) {
        WebSocketResponse pojo = new GsonBuilder().create().fromJson(json, WebSocketResponse.class);

        if (pojo != null
                && pojo.getData() != null
                && pojo.getData().getAttachment() != null) {
            switch (pojo.getData().getAttachment().getType()) {
                case TYPE_QUICK_REPLY:
//                    return convertToQuickReplyModel(json);
                    return null;
                default:
                    return null;

            }
        } else {
            return null;
        }
    }

    private QuickReplyListViewModel convertToQuickReplyModel(String json) {
//        WebSocketResponse pojo = new GsonBuilder().create().fromJson(json, WebSocketResponse.class);

        return new QuickReplyListViewModel(
                0,
                0,
                "",
                "",
                0,
                new Message(),
                "",
                "",
                "",
                TYPE_QUICK_REPLY,
                new FallbackAttachment(),
                convertToQuickReplyList()
                );
    }

    private List<QuickReplyViewModel> convertToQuickReplyList() {
        List<QuickReplyViewModel> list = new ArrayList<>();
        list.add(new QuickReplyViewModel("Tes 1"));
        list.add(new QuickReplyViewModel("Tes 2"));
        list.add(new QuickReplyViewModel("Tes 3"));
        return list;
    }
}
