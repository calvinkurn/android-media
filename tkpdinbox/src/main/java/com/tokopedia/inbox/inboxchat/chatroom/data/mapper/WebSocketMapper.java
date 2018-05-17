package com.tokopedia.inbox.inboxchat.chatroom.data.mapper;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.inboxchat.chatroom.data.ChatWebSocketConstant;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.common.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.common.WebSocketResponseData;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.imageupload.ImageUploadAttributes;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.invoiceselection.InvoiceSingleItemAttributes;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.invoiceselection.InvoicesSelectionPojo;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.invoiceselection.InvoicesSelectionSingleItemPojo;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.invoicesent.InvoiceSentPojo;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.productattachment.ProductAttachmentAttributes;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.quickreply.QuickReplyAttachmentAttributes;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.quickreply.QuickReplyPojo;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.invoiceattachment.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.quickreply.QuickReplyListViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.quickreply.QuickReplyViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.fallback.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.imageupload.ImageUploadViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.invoiceattachment.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.invoiceattachment.AttachInvoiceSingleViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.message.MessageViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.productattachment.ProductAttachmentViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 5/8/18.
 */
public class WebSocketMapper {

    private static final String TYPE_CHAT_RATING = "-1";
    public static final String TYPE_IMAGE_ANNOUNCEMENT = "1";
    public static final String TYPE_IMAGE_UPLOAD = "2";
    public static final String TYPE_PRODUCT_ATTACHMENT = "3";
    public static final String TYPE_INVOICES_SELECTION = "6";
    public static final String TYPE_INVOICE_SEND = "7";
    public static final String TYPE_QUICK_REPLY = "8";
    private SessionHandler sessionHandler;

    @Inject
    public WebSocketMapper(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }


    public BaseChatViewModel map(String json) {
        try {
            WebSocketResponse pojo = new GsonBuilder().create().fromJson(json, WebSocketResponse.class);

            if (pojo.getCode() == ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE) {
                return mapReplyMessage(pojo);
            } else {
                return null;
            }

        } catch (JsonSyntaxException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    private boolean hasAttachment(WebSocketResponse pojo) {
        return pojo.getData() != null
                && pojo.getData().getAttachment() != null
                && pojo.getData().getAttachment().getAttributes() != null;
    }

    private BaseChatViewModel mapReplyMessage(WebSocketResponse pojo) {
        if (pojo.getData().isShowRating() || pojo.getData().getRatingStatus() != 0) {
            return convertToChatRating(pojo.getData());
        }else if (hasAttachment(pojo)) {
            JsonObject jsonAttributes = pojo.getData().getAttachment().getAttributes();
            return mapAttachmentMessage(pojo, jsonAttributes);
        } else {
            return convertToMessageViewModel(pojo.getData());
        }
    }

    private BaseChatViewModel mapAttachmentMessage(WebSocketResponse pojo, JsonObject jsonAttributes) {
        switch (pojo.getData().getAttachment().getType()) {
            case TYPE_QUICK_REPLY:
                return convertToQuickReplyModel(pojo.getData(), jsonAttributes);
            case TYPE_PRODUCT_ATTACHMENT:
                return convertToProductAttachment(pojo.getData(), jsonAttributes);
            case TYPE_IMAGE_UPLOAD:
                return convertToImageUpload(pojo.getData(), jsonAttributes);
            case TYPE_INVOICE_SEND:
                return convertToInvoiceSent(pojo.getData(), jsonAttributes);
            case TYPE_INVOICES_SELECTION:
                return convertToInvoiceSelection(pojo.getData(), jsonAttributes);
            default:
                return convertToFallBackModel(pojo.getData());
        }
    }

    private BaseChatViewModel convertToMessageViewModel(WebSocketResponseData pojo) {
        return new MessageViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                "",
                "",
                pojo.getMessage().getTimeStampUnix(),
                pojo.getStartTime(),
                pojo.getMessage().getCensoredReply(),
                false,
                false,
                isSender(String.valueOf(pojo.getFromUid()))
        );
    }

    private AttachInvoiceSelectionViewModel convertToInvoiceSelection(WebSocketResponseData pojo,
                                                                      JsonObject jsonAttribute) {
        JsonObject jsonObject = jsonAttribute.getAsJsonObject("invoice_list");
        if (jsonObject == null)
            return null;

        InvoicesSelectionPojo invoicesSelectionPojo = new GsonBuilder().create().fromJson
                (jsonObject, InvoicesSelectionPojo.class);
        List<InvoicesSelectionSingleItemPojo> invoiceList = invoicesSelectionPojo.getInvoices();

        ArrayList<AttachInvoiceSingleViewModel> list = new ArrayList<>();

        for (InvoicesSelectionSingleItemPojo invoice : invoiceList) {
            InvoiceSingleItemAttributes attributes = invoice.getAttributes();
            AttachInvoiceSingleViewModel attachInvoice = new AttachInvoiceSingleViewModel(
                    invoice.getType(),
                    invoice.getTypeId(),
                    attributes.getCode(),
                    attributes.getCreatedTime(),
                    attributes.getDescription(),
                    attributes.getUrl(),
                    attributes.getId(),
                    attributes.getImageUrl(),
                    attributes.getStatus(),
                    attributes.getStatusId(),
                    attributes.getTitle(),
                    attributes.getAmount());
            list.add(attachInvoice);
        }

        return new AttachInvoiceSelectionViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                pojo.getAttachment().getId(),
                pojo.getAttachment().getType(),
                pojo.getMessage().getTimeStampUnix(),
                list,
                pojo.getMessage().getCensoredReply()
        );

    }

    private BaseChatViewModel convertToInvoiceSent(WebSocketResponseData pojo, JsonObject
            jsonAttribute) {
        InvoiceSentPojo invoiceSentPojo = new GsonBuilder().create().fromJson(jsonAttribute,
                InvoiceSentPojo.class);
        return new AttachInvoiceSentViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                pojo.getAttachment().getId(),
                pojo.getAttachment().getType(),
                pojo.getMessage().getTimeStampUnix(),
                pojo.getStartTime(),
                pojo.getMessage().getCensoredReply(),
                invoiceSentPojo.getInvoiceLink().getAttributes().getDescription(),
                invoiceSentPojo.getInvoiceLink().getAttributes().getImageUrl(),
                invoiceSentPojo.getInvoiceLink().getAttributes().getTotalAmount(),
                isSender(String.valueOf(pojo.getFromUid()))
        );

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
                pojoAttribute.getThumbnail(),
                pojo.getStartTime(),
                pojo.getMessage().getCensoredReply()
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
                isSender(String.valueOf(pojo.getFromUid())),
                pojo.getMessage().getCensoredReply(),
                pojo.getStartTime()
        );
    }

    private boolean isSender(String fromUid) {
        if (sessionHandler != null) {
            return sessionHandler.getLoginID().equals(fromUid);
        } else {
            return false;
        }
    }
    private BaseChatViewModel convertToChatRating(WebSocketResponseData pojo) {
        return new ChatRatingViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getFromUid()),
                pojo.getFrom(),
                pojo.getFromRole(),
                pojo.getMessage().getCensoredReply(),
                "",
                TYPE_CHAT_RATING,
                pojo.getMessage().getTimeStampUnix(),
                pojo.getRatingStatus(),
                Long.valueOf(pojo.getMessage().getTimeStampUnixNano())
        );
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
