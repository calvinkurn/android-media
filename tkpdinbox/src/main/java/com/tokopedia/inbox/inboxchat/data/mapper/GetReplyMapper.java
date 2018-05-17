package com.tokopedia.inbox.inboxchat.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.domain.WebSocketMapper;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.domain.model.reply.AttachmentInvoice;
import com.tokopedia.inbox.inboxchat.domain.model.reply.AttachmentInvoiceAttributes;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Contact;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ListReply;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.domain.pojo.quickreply.QuickReplyListPojo;
import com.tokopedia.inbox.inboxchat.domain.pojo.quickreply.QuickReplyPojo;
import com.tokopedia.inbox.inboxchat.helper.AttachmentChatHelper;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.imageannouncement.ImageAnnouncementViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.imageupload.ImageUploadViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.invoiceattachment.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.invoiceattachment.AttachInvoiceSingleViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.productattachment.ProductAttachmentViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class GetReplyMapper implements Func1<Response<TkpdResponse>, ChatRoomViewModel> {

    private static final String TOKOPEDIA = "Tokopedia";
    private final SessionHandler sessionHandler;

    public GetReplyMapper(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public ChatRoomViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
                ReplyData data = response.body().convertDataObj(ReplyData.class);

                return mappingToDomain(data);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(MainApplication.getAppContext().getString
                            (R.string.default_request_error_unknown));
                }
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private ChatRoomViewModel mappingToDomain(ReplyData data) {

        ChatRoomViewModel chatRoomViewModel = new ChatRoomViewModel();

        ArrayList<Visitable> list = new ArrayList<>();

        for (ListReply item : data.getList()) {

            if (item.getAttachment() != null
                    && item.getAttachment().getType().equals(WebSocketMapper.TYPE_IMAGE_ANNOUNCEMENT)
                    && item.getRole().contains(TOKOPEDIA)) {
                mapToImageAnnouncement(list, item);
            } else if (item.getAttachment() != null
                    && item.getAttachment().getType().equals(WebSocketMapper.TYPE_IMAGE_UPLOAD)) {
                mapToImageUpload(list, item);
            } else if (item.getAttachment() != null
                    && item.getAttachment().getType().equals(WebSocketMapper.TYPE_PRODUCT_ATTACHMENT)) {
                mapToProductAttachment(list, item);
            } else if (item.getAttachment() != null && item.getAttachment().getType().equals
                    (WebSocketMapper.TYPE_QUICK_REPLY)) {
                QuickReplyListViewModel quickReplyListViewModel = new QuickReplyListViewModel(
                        String.valueOf(item.getMsgId()),
                        String.valueOf(item.getSenderId()),
                        item.getSenderName(),
                        item.getRole(),
                        item.getMsg(),
                        item.getAttachment().getId(),
                        item.getAttachment().getType(),
                        item.getReplyTime(),
                        convertQuickItemChatList(item.getAttachment().getQuickReplies())
                );
                list.add(quickReplyListViewModel);
            } else if (item.getAttachment() != null && item.getAttachment().getType().equals
                    (WebSocketMapper.TYPE_INVOICE_SEND)) {
                mapToInvoiceSend(list, item);
            }else if(item.getAttachment() != null && item.getAttachment().getType().equals
                    (WebSocketMapper.TYPE_INVOICES_SELECTION)){
                mapToInvoiceSelectionAttachment(list,item);
            }
            else if (!item.isOpposite()) {
                MyChatViewModel temp = new MyChatViewModel();
                temp.setReplyId(item.getReplyId());
                temp.setSenderId(item.getSenderId());
                temp.setMsg(item.getMsg());
                temp.setReplyTime(item.getReplyTime());
                temp.setFraudStatus(item.getFraudStatus());
                temp.setReadTime(item.getReadTime());
                temp.setAttachmentId(item.getAttachmentId());
                temp.setOldMsgId(item.getOldMsgId());
                temp.setMsgId(item.getMsgId());
                temp.setRole(item.getRole());
                temp.setSenderName(item.getSenderName());
                temp.setHighlight(item.isHighlight());
                temp.setOldMessageTitle(item.getOldMessageTitle());
                if (item.isHighlight()) {
                    temp.setSpanned(MethodChecker.fromHtml(item.getMsg()));
                }
                temp.setAttachment(item.getAttachment());
                temp.setReadStatus(item.isMessageIsRead());

                list.add(temp);
            } else {
                OppositeChatViewModel temp = new OppositeChatViewModel();
                temp.setReplyId(item.getReplyId());
                temp.setSenderId(item.getSenderId());
                temp.setMsg(item.getMsg());
                temp.setReplyTime(item.getReplyTime());
                temp.setFraudStatus(item.getFraudStatus());
                temp.setReadTime(item.getReadTime());
                temp.setAttachmentId(item.getAttachmentId());
                temp.setOldMsgId(item.getOldMsgId());
                temp.setMsgId(item.getMsgId());
                temp.setRole(item.getRole());
                temp.setSenderName(item.getSenderName());
                temp.setHighlight(item.isHighlight());
                temp.setOldMessageTitle(item.getOldMessageTitle());
                temp.setShowRating(item.isShowRating());
                temp.setRatingStatus(item.getRatingStatus());
                temp.setReplyTimeNano(Long.parseLong(item.getReplyTimeNano()));
                if (item.isHighlight()) {
                    temp.setSpanned(MethodChecker.fromHtml(item.getMsg()));
                }
                temp.setAttachment(item.getAttachment());
                list.add(temp);
            }

        }
        Collections.reverse(list);
        chatRoomViewModel.setChatList(list);
        chatRoomViewModel.setHasNext(data.isHasNext());
        chatRoomViewModel.setTextAreaReply(data.getTextAreaReply());
        if (data.getContacts().size() > 0) {
            chatRoomViewModel.setShopId(data.getContacts().get(0).getShopId());
        }
        setOpponentViewModel(chatRoomViewModel, data.getContacts());
        return chatRoomViewModel;
    }

    private void mapToImageUpload(ArrayList<Visitable> list, ListReply item) {
        ImageUploadViewModel imageUpload = new ImageUploadViewModel(
                String.valueOf(item.getMsgId()),
                item.getSenderId(),
                item.getSenderName(),
                item.getRole(),
                item.getAttachment().getId(),
                item.getAttachment().getType(),
                item.getReplyTime(),
                isSender(item.getSenderId()),
                item.getAttachment().getAttributes().getImageUrl(),
                item.getAttachment().getAttributes().getThumbnail(),
                item.isMessageIsRead()
        );

        list.add(imageUpload);
    }

    private void mapToImageAnnouncement(ArrayList<Visitable> list, ListReply item) {
        ImageAnnouncementViewModel imageAnnouncement = new ImageAnnouncementViewModel(
                String.valueOf(item.getMsgId()),
                item.getSenderId(),
                item.getSenderName(),
                item.getRole(),
                item.getAttachment().getId(),
                item.getAttachment().getType(),
                item.getReplyTime(),
                item.getAttachment().getAttributes().getImageUrl(),
                item.getAttachment().getAttributes().getUrl()
        );

        list.add(imageAnnouncement);
    }

    private void mapToInvoiceSend(ArrayList<Visitable> list, ListReply item) {
        AttachmentInvoiceAttributes invoiceAttributes =
                item.getAttachment().getAttributes().getInvoiceLink().getAttributes();
        AttachInvoiceSentViewModel model = new AttachInvoiceSentViewModel(
                String.valueOf(item.getMsgId()),
                item.getSenderId(),
                item.getSenderName(),
                item.getRole(),
                item.getAttachment().getId(),
                item.getAttachment().getType(),
                item.getReplyTime(),
                item.getMsg(),
                invoiceAttributes.getDescription(),
                invoiceAttributes.getImageUrl(),
                invoiceAttributes.getAmount(),
                isSender(item.getSenderId()),
                item.isMessageIsRead()
        );

        list.add(model);
    }

    private void mapToProductAttachment(ArrayList<Visitable> list, ListReply item) {

        ProductAttachmentViewModel productAttachment = new ProductAttachmentViewModel(
                String.valueOf(item.getMsgId()),
                item.getSenderId(),
                item.getSenderName(),
                item.getRole(),
                item.getAttachment().getId(),
                item.getAttachment().getType(),
                item.getReplyTime(),
                item.isMessageIsRead(),
                item.getAttachment().getAttributes().getProductId(),
                item.getAttachment().getAttributes().getProductProfile().getName(),
                item.getAttachment().getAttributes().getProductProfile().getPrice(),
                item.getAttachment().getAttributes().getProductProfile().getUrl(),
                item.getAttachment().getAttributes().getProductProfile().getImageUrl(),
                isSender(item.getSenderId())
        );

        list.add(productAttachment);
    }

    private void mapToInvoiceSelectionAttachment(ArrayList<Visitable> list, ListReply item) {
        Attachment attachment = item.getAttachment();
        if (attachment.getType().equals(AttachmentChatHelper.INVOICE_LIST_ATTACHED)) {
            ArrayList<AttachInvoiceSingleViewModel> listSingleInvoice = new ArrayList<>();
            for (AttachmentInvoice invoice : attachment.getAttributes().getInvoices()) {
                listSingleInvoice.add(new AttachInvoiceSingleViewModel(
                        invoice.getTypeString(),
                        invoice.getType(),
                        invoice.getAttributes().getCode(),
                        invoice.getAttributes().getCreatedTime(),
                        invoice.getAttributes().getDescription(),
                        invoice.getAttributes().getUrl(),
                        invoice.getAttributes().getId(),
                        invoice.getAttributes().getImageUrl(),
                        invoice.getAttributes().getStatus(),
                        invoice.getAttributes().getStatusId(),
                        invoice.getAttributes().getTitle(),
                        invoice.getAttributes().getAmount()
                ));
            }
            AttachInvoiceSelectionViewModel invoiceSelectionViewModel = new
                    AttachInvoiceSelectionViewModel(String.valueOf(item.getMsgId()),
                    item.getSenderId(),
                    item.getSenderName(),
                    item.getRole(),
                    String.valueOf(item.getAttachmentId()),
                    attachment.getType(),
                    item.getReplyTime(),
                    listSingleInvoice);
            list.add(invoiceSelectionViewModel);
        }
    }


    private void setOpponentViewModel(ChatRoomViewModel chatRoomViewModel, List<Contact>
            contacts) {
        for (Contact contact : contacts) {
            if (contact.getUserId() != 0
                    && !String.valueOf(contact.getUserId()).equals(sessionHandler.getLoginID())) {

                if (!TextUtils.isEmpty(contact.getAttributes().getName())) {
                    chatRoomViewModel.setNameHeader(contact.getAttributes().getName());
                }

                if (!TextUtils.isEmpty(contact.getAttributes().getThumbnail())) {
                    chatRoomViewModel.setImageHeader(contact.getAttributes().getThumbnail());
                }

            }
        }
    }

    private List<QuickReplyViewModel> convertQuickItemChatList(QuickReplyListPojo pojoList) {

        List<QuickReplyViewModel> list = new ArrayList<>();
        if (pojoList != null) {
            for (QuickReplyPojo pojo : pojoList.getQuickReplies()) {
                QuickReplyViewModel model = new QuickReplyViewModel(pojo.getMessage());
                list.add(model);
            }
        }
        return list;
    }

    private boolean isSender(String senderId) {
        return senderId.equals(sessionHandler.getLoginID());
    }
}