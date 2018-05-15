package com.tokopedia.inbox.inboxchat.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.adapter.viewholder.AttachedInvoiceSentViewHolder;
import com.tokopedia.inbox.inboxchat.adapter.viewholder.FallbackAttachmentViewHolder;
import com.tokopedia.inbox.inboxchat.adapter.viewholder.ImageAnnouncementViewHolder;
import com.tokopedia.inbox.inboxchat.adapter.viewholder.ProductAttachmentViewHolder;
import com.tokopedia.inbox.inboxchat.adapter.viewholder.QuickReplyViewHolder;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.FallbackAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.fragment.ChatRoomFragment;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewholder.AttachedInvoiceSelectionViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.ImageUploadViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.MyChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.OppositeChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.TimeMachineChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.TypingChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.ImageUploadViewModel;
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

public class ChatRoomTypeFactoryImpl extends BaseAdapterTypeFactory implements ChatRoomTypeFactory {

    ChatRoomContract.View viewListener;

    public ChatRoomTypeFactoryImpl(ChatRoomFragment context) {
        this.viewListener = context;
    }

    @Override
    public int type(OppositeChatViewModel oppositeChatViewModel) {
        return OppositeChatViewHolder.LAYOUT;
    }

    @Override
    public int type(MyChatViewModel myChatViewModel) {
        return MyChatViewHolder.LAYOUT;
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

        if (type == OppositeChatViewHolder.LAYOUT)
            viewHolder = new OppositeChatViewHolder(view, viewListener);
        else if (type == MyChatViewHolder.LAYOUT)
            viewHolder = new MyChatViewHolder(view, viewListener);
        else if (type == TimeMachineChatViewHolder.LAYOUT)
            viewHolder = new TimeMachineChatViewHolder(view, viewListener);
        else if (type == TypingChatViewHolder.LAYOUT)
            viewHolder = new TypingChatViewHolder(view);
        else if (type == ImageUploadViewHolder.LAYOUT)
            viewHolder = new ImageUploadViewHolder(view);
        else if (type == AttachedInvoiceSentViewHolder.LAYOUT)
            viewHolder = new AttachedInvoiceSentViewHolder(view, viewListener);
        else if (type == AttachedInvoiceSelectionViewHolder.LAYOUT)
            viewHolder = new AttachedInvoiceSelectionViewHolder(view, viewListener);
        else if (type == QuickReplyViewHolder.LAYOUT)
            viewHolder = new QuickReplyViewHolder(view, viewListener);
        else if (type == FallbackAttachmentViewHolder.LAYOUT)
            viewHolder = new FallbackAttachmentViewHolder(view, viewListener);
        else if (type == ProductAttachmentViewHolder.LAYOUT)
            viewHolder = new ProductAttachmentViewHolder(view, viewListener);
        else if (type == ImageAnnouncementViewHolder.LAYOUT)
            viewHolder = new ImageAnnouncementViewHolder(view, viewListener);
        else
            return super.createViewHolder(view, type);

        return viewHolder;
    }

}
