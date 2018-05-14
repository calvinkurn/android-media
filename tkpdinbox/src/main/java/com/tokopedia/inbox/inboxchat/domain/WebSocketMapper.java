package com.tokopedia.inbox.inboxchat.domain;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.domain.pojo.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.domain.pojo.WebSocketResponseData;
import com.tokopedia.inbox.inboxchat.domain.pojo.quickreply.QuickReplyAttachment;
import com.tokopedia.inbox.inboxchat.domain.pojo.quickreply.QuickReplyPojo;
import com.tokopedia.inbox.inboxchat.domain.pojo.quickreply.QuickReplyWebSocketResponse;
import com.tokopedia.inbox.inboxchat.domain.pojo.quickreply.QuickReplyWebSocketResponseData;
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
    private static final String TYPE_PRODUCT_ATTACHMENT = "3";


    @Inject
    public WebSocketMapper() {
    }


    public BaseChatViewModel map(String json) {
        try {

            WebSocketResponse pojo = new GsonBuilder().create().fromJson(json, WebSocketResponse.class);

            if (pojo != null
                    && pojo.getData() != null
                    && pojo.getData().getAttachment() != null) {
                switch (pojo.getData().getAttachment().getType()) {
                    case TYPE_QUICK_REPLY:
                        return convertToQuickReplyModel(json);
                    case TYPE_PRODUCT_ATTACHMENT:
//                        return convertToProductAttachment(json);
                    default:
//                        return convertToFallBackModel(pojo.getData());
                        return null;

                }
            } else {
                return null;
            }
        } catch (JsonSyntaxException e) {
            return null;
        }

    }

    private BaseChatViewModel convertToProductAttachment(String json) {
        return null;
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

    private QuickReplyListViewModel convertToQuickReplyModel(String json) {
        QuickReplyWebSocketResponse pojo = new GsonBuilder().create().fromJson(json, QuickReplyWebSocketResponse.class);
        QuickReplyWebSocketResponseData data = pojo.getData();
        return new QuickReplyListViewModel(
                String.valueOf(data.getMsgId()),
                String.valueOf(data.getFromUid()),
                data.getFrom(),
                data.getFromRole(),
                data.getMessage().getCensoredReply(),
                data.getAttachment().getId(),
                TYPE_QUICK_REPLY,
                data.getMessage().getTimeStampUnix(),
                convertToQuickReplyList(data.getAttachment())
        );
    }

    private List<QuickReplyViewModel> convertToQuickReplyList(QuickReplyAttachment quickReplyListPojo) {
        List<QuickReplyViewModel> list = new ArrayList<>();
        if (quickReplyListPojo != null
                && quickReplyListPojo.getAttributes() != null
                && quickReplyListPojo.getAttributes().getQuickReplies() != null
                && !quickReplyListPojo.getAttributes().getQuickReplies().isEmpty()) {
            for (QuickReplyPojo pojo : quickReplyListPojo.getAttributes().getQuickReplies()) {
                if (pojo.getMessage() != null && !TextUtils.isEmpty(pojo.getMessage())) {
                    list.add(new QuickReplyViewModel(pojo.getMessage()));
                }
            }
        }
        return list;
    }
}
