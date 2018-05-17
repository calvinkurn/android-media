package com.tokopedia.inbox.inboxchat.chatroom.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.adapter.viewholder.chatbot.AttachedInvoiceSelectionViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.adapter.viewholder.chatbot.AttachedInvoiceSentViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.adapter.viewholder.chatbot.QuickReplyViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.adapter.viewholder.chatroom.ImageAnnouncementViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.adapter.viewholder.chatroom.ProductAttachmentViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.adapter.viewholder.chatroom.TimeMachineChatViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.adapter.viewholder.common.FallbackAttachmentViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.adapter.viewholder.common.ImageUploadViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.adapter.viewholder.common.MessageViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.adapter.viewholder.chatroom.TypingChatViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.fragment.ChatRoomFragment;
import com.tokopedia.inbox.inboxchat.chatroom.listener.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.invoiceattachment.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.TypingChatModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.TimeMachineChatModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.fallback.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.imageannouncement.ImageAnnouncementViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.imageupload.ImageUploadViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.invoiceattachment.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.message.MessageViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.productattachment.ProductAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.viewmodel.quickreply.QuickReplyListViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class ChatRoomTypeFactoryImpl extends BaseAdapterTypeFactory implements ChatRoomTypeFactory {

    ChatRoomContract.View viewListener;

    public ChatRoomTypeFactoryImpl(ChatRoomFragment context) {
        this.viewListener = context;
    }

    @Override
    public int type(TimeMachineChatModel timeMachineChatModel) {
        return TimeMachineChatViewHolder.LAYOUT;
    }

    @Override
    public int type(TypingChatModel typingChatModel) {
        return TypingChatViewHolder.LAYOUT;
    }

    @Override
    public int type(ImageUploadViewModel attachImageModel) {
        return ImageUploadViewHolder.LAYOUT;
    }

    @Override
    public int type(MessageViewModel messageViewModel) {
        return MessageViewHolder.LAYOUT;
    }

    @Override
    public int type(AttachInvoiceSentViewModel attachInvoiceSentViewModel) {
        return AttachedInvoiceSentViewHolder.LAYOUT;
    }


    @Override
    public int type(AttachInvoiceSelectionViewModel attachInvoiceSelectionViewModel) {
        return AttachedInvoiceSelectionViewHolder.LAYOUT;
    }

    @Override
    public int type(QuickReplyListViewModel quickReplyListViewModel) {
        return QuickReplyViewHolder.LAYOUT;
    }

    @Override
    public int type(FallbackAttachmentViewModel fallbackAttachmentViewModel) {
        return FallbackAttachmentViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatRatingViewModel chatRatingViewModel) {
        return ChatRatingViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductAttachmentViewModel productAttachmentViewModel) {
        return ProductAttachmentViewHolder.LAYOUT;
    }

    @Override
    public int type(ImageAnnouncementViewModel imageAnnouncementViewModel) {
        return ImageAnnouncementViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == TimeMachineChatViewHolder.LAYOUT)
            viewHolder = new TimeMachineChatViewHolder(view, viewListener);
        else if (type == TypingChatViewHolder.LAYOUT)
            viewHolder = new TypingChatViewHolder(view);
        else if (type == MessageViewHolder.LAYOUT)
            viewHolder = new MessageViewHolder(view, viewListener);
        else if (type == ImageUploadViewHolder.LAYOUT)
            viewHolder = new ImageUploadViewHolder(view, viewListener);
        else if (type == AttachedInvoiceSentViewHolder.LAYOUT)
            viewHolder = new AttachedInvoiceSentViewHolder(view, viewListener);
        else if (type == AttachedInvoiceSelectionViewHolder.LAYOUT)
            viewHolder = new AttachedInvoiceSelectionViewHolder(view, viewListener);
        else if (type == QuickReplyViewHolder.LAYOUT)
            viewHolder = new QuickReplyViewHolder(view, viewListener);
        else if (type == FallbackAttachmentViewHolder.LAYOUT)
            viewHolder = new FallbackAttachmentViewHolder(view, viewListener);
        else if (type == ChatRatingViewHolder.LAYOUT)
            viewHolder = new ChatRatingViewHolder(view, viewListener);
        else if (type == ProductAttachmentViewHolder.LAYOUT)
            viewHolder = new ProductAttachmentViewHolder(view, viewListener);
        else if (type == ImageAnnouncementViewHolder.LAYOUT)
            viewHolder = new ImageAnnouncementViewHolder(view, viewListener);
        else
            return super.createViewHolder(view, type);

        return viewHolder;
    }

}
