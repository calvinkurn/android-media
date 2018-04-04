package com.tokopedia.inbox.inboxchat.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Contact;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ListReply;
import com.tokopedia.inbox.inboxchat.domain.model.reply.ReplyData;
import com.tokopedia.inbox.inboxchat.helper.AttachmentChatHelper;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachProductViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.ThumbnailChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.mapper.AttachInvoiceMapper;

import java.util.ArrayList;
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
            if (item.getRole().contains(TOKOPEDIA)) {
                ThumbnailChatViewModel temp = new ThumbnailChatViewModel();
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
                list.add(checkAndConvertItemModelToAttachmentType(temp,temp.getAttachment()));
            }else {
                if (!item.isOpposite()) {
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

                    list.add(checkAndConvertItemModelToAttachmentType(temp,temp.getAttachment()));
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
                    if (item.isHighlight()) {
                        temp.setSpanned(MethodChecker.fromHtml(item.getMsg()));
                    }
                    temp.setAttachment(item.getAttachment());
                    list.add(checkAndConvertItemModelToAttachmentType(temp,temp.getAttachment()));
                }
            }
        }

        chatRoomViewModel.setChatList(list);
        chatRoomViewModel.setHasNext(data.isHasNext());
        chatRoomViewModel.setTextAreaReply(data.getTextAreaReply());
        setOpponentViewModel(chatRoomViewModel, data.getContacts());
        return chatRoomViewModel;
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

    private Visitable checkAndConvertItemModelToAttachmentType(Visitable input, Attachment attachment){
        if(attachment == null) return input;

        if(attachment.getType().equals(AttachmentChatHelper.PRODUCT_ATTACHED)) {
            if ((input instanceof MyChatViewModel)) {
                return new AttachProductViewModel((MyChatViewModel) input);
            } else if (input instanceof OppositeChatViewModel) {
                return new AttachProductViewModel((OppositeChatViewModel) input);
            } else if (input instanceof ThumbnailChatViewModel) {
                return new AttachProductViewModel((ThumbnailChatViewModel) input);
            }
        }
        else if(attachment.getType().equals(AttachmentChatHelper.INVOICE_ATTACHED)) {
            if(attachment.getAttributes().getInvoices() == null){
                if ((input instanceof MyChatViewModel)) {
                    return new AttachInvoiceSentViewModel((MyChatViewModel) input);
                } else if (input instanceof OppositeChatViewModel) {
                    return new AttachInvoiceSentViewModel((OppositeChatViewModel) input);
                } else if (input instanceof ThumbnailChatViewModel) {
                    return new AttachInvoiceSentViewModel((ThumbnailChatViewModel) input);
                }
            }
            else {
                return AttachInvoiceMapper.attachmentToAttachInvoiceSelectionModel(attachment);
            }
        }
        return input;
    }
}