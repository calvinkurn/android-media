package com.tokopedia.inbox.inboxchat.domain;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.domain.pojo.common.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.domain.pojo.common.WebSocketResponseData;
import com.tokopedia.inbox.inboxchat.domain.pojo.quickreply.QuickReplyAttachmentAttributes;
import com.tokopedia.inbox.inboxchat.domain.pojo.quickreply.QuickReplyPojo;
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

            String jsonAttributes = pojo.getData().getAttachment().getAttributes();
            if (pojo != null
                    && pojo.getData() != null
                    && pojo.getData().getAttachment() != null
                    && !TextUtils.isEmpty(jsonAttributes)) {
                switch (pojo.getData().getAttachment().getType()) {
                    case TYPE_QUICK_REPLY:
                        return convertToQuickReplyModel(pojo.getData(), jsonAttributes);
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

    private QuickReplyListViewModel convertToQuickReplyModel(WebSocketResponseData pojo, String
            jsonAttribute) {
        QuickReplyAttachmentAttributes pojoAttribute = new GsonBuilder().create().fromJson(jsonAttribute,
                QuickReplyAttachmentAttributes.class);
        return new QuickReplyListViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                pojo.getMessage().getCensoredReply(),
                pojo.getAttachment().getId(),
                TYPE_QUICK_REPLY,
                pojo.getMessage().getTimeStampUnix(),
                convertToQuickReplyList(pojoAttribute)
        );
    }

    private List<QuickReplyViewModel> convertToQuickReplyList(QuickReplyAttachmentAttributes quickReplyListPojo) {
        List<QuickReplyViewModel> list = new ArrayList<>();
        if (quickReplyListPojo != null
                && !quickReplyListPojo.getQuickReplies().isEmpty()) {
            for (QuickReplyPojo pojo : quickReplyListPojo.getQuickReplies()) {
                if (pojo.getMessage() != null && !TextUtils.isEmpty(pojo.getMessage())) {
                    list.add(new QuickReplyViewModel(pojo.getMessage()));
                }
            }
        }
        return list;
    }
}
