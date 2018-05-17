package com.tokopedia.inbox.inboxchat.chatroom.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.invoiceattachment.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.invoiceattachment.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.TypingChatModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.quickreply.QuickReplyListViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.TimeMachineChatModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.fallback.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.imageannouncement.ImageAnnouncementViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.imageupload.ImageUploadViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.message.MessageViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.productattachment.ProductAttachmentViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public interface ChatRoomTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(AttachInvoiceSentViewModel attachInvoiceSentViewModel);

    int type(AttachInvoiceSelectionViewModel attachInvoiceSelectionViewModel);

    // NEW VERSION

    int type(QuickReplyListViewModel quickReplyListViewModel);

    int type(FallbackAttachmentViewModel fallbackAttachmentViewModel);

    int type(ChatRatingViewModel chatRatingViewModel);

    int type(ProductAttachmentViewModel productAttachmentViewModel);

    int type(ImageAnnouncementViewModel imageAnnouncementViewModel);

    int type(ImageUploadViewModel attachImageModel);

    int type(MessageViewModel messageViewModel);

    //OTHER
    int type(TimeMachineChatModel timeMachineChatModel);

    int type(TypingChatModel typingChatModel);


}
