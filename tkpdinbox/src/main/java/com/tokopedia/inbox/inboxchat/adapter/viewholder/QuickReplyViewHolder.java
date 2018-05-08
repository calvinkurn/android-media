package com.tokopedia.inbox.inboxchat.adapter.viewholder;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.QuickReplyListViewModel;

/**
 * @author by nisie on 5/8/18.
 */
public class QuickReplyViewHolder extends AbstractViewHolder<QuickReplyListViewModel> {
    public static final int LAYOUT = R.layout.quick_reply_chat_layout;

    public QuickReplyViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(QuickReplyListViewModel element) {

    }
}
