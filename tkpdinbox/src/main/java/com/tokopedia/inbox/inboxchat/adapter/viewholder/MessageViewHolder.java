package com.tokopedia.inbox.inboxchat.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewholder.movement.ChatLinkHandlerMovementMethod;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.message.MessageViewModel;

/**
 * @author by nisie on 5/16/18.
 */
public class MessageViewHolder extends BaseChatViewHolder<MessageViewModel> {

    View view;
    TextView message;
    ImageView chatStatus;

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_message_chat;

    public MessageViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
        message = itemView.findViewById(R.id.message);
        message.setMovementMethod(new ChatLinkHandlerMovementMethod(viewListener));

    }

    @Override
    public void bind(MessageViewModel element) {
        super.bind(element);

        setReadStatus(element);
    }

    private void setReadStatus(MessageViewModel element) {
        int imageResource;

        if(element.isRead()){
            imageResource = R.drawable.ic_chat_read;
        }else {
            imageResource = R.drawable.ic_chat_unread;
        }
        if(element.isDummy()){
            imageResource = R.drawable.ic_chat_pending;
        }

    }
}
