package com.tokopedia.inbox.inboxchat.domain;

import com.google.gson.GsonBuilder;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponseData;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 5/8/18.
 */
public class WebSocketMapper {

    private static final String TYPE_QUICK_REPLY = "8";

    @Inject
    public WebSocketMapper() {
    }


    public BaseChatViewModel map(String json) {
        WebSocketResponse pojo = new GsonBuilder().create().fromJson(json, WebSocketResponse.class);

        if (pojo != null
                && pojo.getData() != null
                && pojo.getData().getAttachment() != null) {
            switch (pojo.getData().getAttachment().getType()) {
                case TYPE_QUICK_REPLY:
                    return convertToQuickReplyModel(pojo.getData());
                default:
                    return convertToFallBackModel(pojo.getData());
            }
        } else {
            return null;
        }
    }

    private BaseChatViewModel convertToFallBackModel(WebSocketResponseData pojo) {
        return new FallbackAttachmentViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                pojo.getAttachment().getFallbackAttachment().getMessage(),
                pojo.getAttachment().getId(),
                pojo.getAttachment().getType(),
                pojo.getMessage().getTimeStampUnix(),
                pojo.getAttachment().getFallbackAttachment().getMessage(),
                pojo.getAttachment().getFallbackAttachment().getUrl(),
                pojo.getAttachment().getFallbackAttachment().getSpan(),
                pojo.getAttachment().getFallbackAttachment().getHtml()
        );
    }

    private QuickReplyListViewModel convertToQuickReplyModel(WebSocketResponseData pojo) {
        return new QuickReplyListViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                pojo.getMessage().getCensoredReply(),
                pojo.getAttachment().getId(),
                TYPE_QUICK_REPLY,
                pojo.getMessage().getTimeStampUnix(),
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
