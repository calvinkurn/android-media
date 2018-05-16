package com.tokopedia.inbox.inboxchat.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.fallback.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.imageupload.ImageUploadViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TypingChatModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.TimeMachineChatModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.imageannouncement.ImageAnnouncementViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.productattachment.ProductAttachmentViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public interface ChatRoomTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(OppositeChatViewModel oppositeChatViewModel);

    int type(MyChatViewModel myChatViewModel);

    int type(AttachInvoiceSentViewModel attachInvoiceSentViewModel);

    int type(AttachInvoiceSelectionViewModel attachInvoiceSelectionViewModel);

    // NEW VERSION

    int type(QuickReplyListViewModel quickReplyListViewModel);

    int type(FallbackAttachmentViewModel fallbackAttachmentViewModel);

    int type(ProductAttachmentViewModel productAttachmentViewModel);

    int type(ImageAnnouncementViewModel imageAnnouncementViewModel);

    int type(ImageUploadViewModel attachImageModel);

    //OTHER
    int type(TimeMachineChatModel timeMachineChatModel);

    int type(TypingChatModel typingChatModel);


}
