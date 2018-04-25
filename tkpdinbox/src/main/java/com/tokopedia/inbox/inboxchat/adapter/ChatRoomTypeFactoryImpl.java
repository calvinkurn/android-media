package com.tokopedia.inbox.inboxchat.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.fragment.ChatRoomFragment;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewholder.AttachImageViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.AttachedInvoiceSelectionViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.AttachedInvoiceSentViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.AttachedProductViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.MyChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.OppositeChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.ThumbnailChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.TimeMachineChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.TypingChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachImageModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachProductViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.ThumbnailChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TypingChatModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.TimeMachineChatModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class ChatRoomTypeFactoryImpl extends BaseAdapterTypeFactory implements ChatRoomTypeFactory{

    ChatRoomContract.View viewListener;

    public ChatRoomTypeFactoryImpl(ChatRoomFragment context) {
        this.viewListener = context;
    }

    @Override
    public int type(ThumbnailChatViewModel thumbnailChatViewModel) {
        return ThumbnailChatViewHolder.LAYOUT;
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
    public int type(AttachImageModel attachImageModel) {
        return AttachImageViewHolder.LAYOUT;
    }

    @Override
    public int type(AttachProductViewModel attachProductViewModel) {
        return AttachedProductViewHolder.LAYOUT;
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
        else if (type == AttachImageViewHolder.LAYOUT)
            viewHolder = new AttachImageViewHolder(view);
        else if (type == ThumbnailChatViewHolder.LAYOUT)
            viewHolder = new ThumbnailChatViewHolder(view, viewListener);
        else if (type == AttachedProductViewHolder.LAYOUT)
            viewHolder = new AttachedProductViewHolder(view,viewListener);
        else if(type == AttachedInvoiceSentViewHolder.LAYOUT)
            viewHolder = new AttachedInvoiceSentViewHolder(view);
        else if(type == AttachedInvoiceSelectionViewHolder.LAYOUT)
            viewHolder = new AttachedInvoiceSelectionViewHolder(view, viewListener);
        else
            return super.createViewHolder(view, type);

        return viewHolder;
    }

}
