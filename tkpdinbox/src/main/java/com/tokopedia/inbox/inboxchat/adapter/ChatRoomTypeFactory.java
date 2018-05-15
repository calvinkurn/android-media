package com.tokopedia.inbox.inboxchat.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachImageModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.ThumbnailChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TypingChatModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.TimeMachineChatModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.productattachment.ProductAttachmentViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public interface ChatRoomTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(ThumbnailChatViewModel thumbnailChatViewModel);

    int type(OppositeChatViewModel oppositeChatViewModel);

    int type(MyChatViewModel myChatViewModel);

    int type(TimeMachineChatModel timeMachineChatModel);

    int type(TypingChatModel typingChatModel);

    int type(AttachImageModel attachImageModel);

    int type(AttachInvoiceSentViewModel attachInvoiceSentViewModel);

    int type(AttachInvoiceSelectionViewModel attachInvoiceSelectionViewModel);

    int type(QuickReplyListViewModel quickReplyListViewModel);

    int type(FallbackAttachmentViewModel fallbackAttachmentViewModel);

    int type(ProductAttachmentViewModel productAttachmentViewModel);

}
