package com.tokopedia.inbox.inboxchat.domain;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.domain.pojo.common.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.domain.pojo.common.WebSocketResponseData;
import com.tokopedia.inbox.inboxchat.domain.pojo.imageupload.ImageUploadAttributes;
import com.tokopedia.inbox.inboxchat.domain.pojo.productattachment.ProductAttachmentAttributes;
import com.tokopedia.inbox.inboxchat.domain.pojo.quickreply.QuickReplyAttachmentAttributes;
import com.tokopedia.inbox.inboxchat.domain.pojo.quickreply.QuickReplyPojo;
import com.tokopedia.inbox.inboxchat.viewmodel.ImageUploadViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.productattachment.ProductAttachmentViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 5/8/18.
 */
public class WebSocketMapper {

    public static final String TYPE_IMAGE_ANNOUNCEMENT = "1";
    public static final String TYPE_IMAGE_UPLOAD = "2";
    public static final String TYPE_PRODUCT_ATTACHMENT = "3";
    public static final String TYPE_QUICK_REPLY = "8";
    private SessionHandler sessionHandler;

    @Inject
    public WebSocketMapper(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }


    public BaseChatViewModel map(String json) {
        try {

            WebSocketResponse pojo = new GsonBuilder().create().fromJson(json, WebSocketResponse.class);

            JsonObject jsonAttributes = pojo.getData().getAttachment().getAttributes();
            if (pojo.getData() != null
                    && pojo.getData().getAttachment() != null
                    && jsonAttributes != null) {
                switch (pojo.getData().getAttachment().getType()) {
                    case TYPE_QUICK_REPLY:
                        return convertToQuickReplyModel(pojo.getData(), jsonAttributes);
                    case TYPE_PRODUCT_ATTACHMENT:
                        return convertToProductAttachment(pojo.getData(), jsonAttributes);
                    case TYPE_IMAGE_UPLOAD:
                        return convertToImageUpload(pojo.getData(), jsonAttributes);
                    default:
//                        return convertToFallBackModel(pojo.getData());
                        return null;

                }
            } else {
                return null;
            }
        } catch (JsonSyntaxException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }

    }

    private BaseChatViewModel convertToImageUpload(WebSocketResponseData pojo, JsonObject
            jsonAttribute) {
        ImageUploadAttributes pojoAttribute = new GsonBuilder().create().fromJson(jsonAttribute,
                ImageUploadAttributes.class);

        return new ImageUploadViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                pojo.getAttachment().getId(),
                pojo.getAttachment().getType(),
                pojo.getMessage().getTimeStampUnix(),
                isSender(String.valueOf(pojo.getFromUid())),
                pojoAttribute.getImageUrl(),
                pojoAttribute.getThumbnail()
        );
    }

    private BaseChatViewModel convertToProductAttachment(WebSocketResponseData pojo, JsonObject jsonAttribute) {
        ProductAttachmentAttributes pojoAttribute = new GsonBuilder().create().fromJson(jsonAttribute,
                ProductAttachmentAttributes.class);

        return new ProductAttachmentViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                pojo.getAttachment().getId(),
                pojo.getAttachment().getType(),
                pojo.getMessage().getTimeStampUnix(),
                pojoAttribute.getProductId(),
                pojoAttribute.getProductProfile().getName(),
                pojoAttribute.getProductProfile().getPrice(),
                pojoAttribute.getProductProfile().getUrl(),
                pojoAttribute.getProductProfile().getImageUrl(),
                isSender(String.valueOf(pojo.getFromUid()))
        );
    }

    private boolean isSender(String fromUid) {
        if (sessionHandler != null) {
            return sessionHandler.getLoginID().equals(fromUid);
        } else {
            return false;
        }
    }

    private BaseChatViewModel convertToFallBackModel(WebSocketResponseData pojo) {
        return new FallbackAttachmentViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                pojo.getAttachment().getId(),
                pojo.getAttachment().getType(),
                pojo.getMessage().getTimeStampUnix(),
                pojo.getAttachment().getFallbackAttachment().getMessage(),
                pojo.getAttachment().getFallbackAttachment().getUrl(),
                pojo.getAttachment().getFallbackAttachment().getSpan(),
                pojo.getAttachment().getFallbackAttachment().getHtml()
        );
    }

    private QuickReplyListViewModel convertToQuickReplyModel(WebSocketResponseData pojo, JsonObject
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
