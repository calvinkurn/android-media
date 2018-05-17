package com.tokopedia.inbox.inboxchat.chatroom.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.TimeMachineChatModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.TypingChatModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.fallback.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.imageannouncement.ImageAnnouncementViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.imageupload.ImageUploadViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.invoiceattachment.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.invoiceattachment.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.message.MessageViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.productattachment.ProductAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.quickreply.QuickReplyListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.ChatRatingViewModel;

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
