package com.tokopedia.inbox.inboxchat.adapter.viewholder;

import android.view.View;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyListViewModel;

/**
 * @author by nisie on 5/8/18.
 */
public class QuickReplyViewHolder extends BaseChatViewHolder<QuickReplyListViewModel> {
    public static final int LAYOUT = R.layout.quick_reply_chat_layout;

    public QuickReplyViewHolder(View itemView, ChatRoomContract.View viewListener) {
        super(itemView, viewListener);
    }

    @Override
    public void bind(QuickReplyListViewModel element) {
        super.bind(element);
    }
}
