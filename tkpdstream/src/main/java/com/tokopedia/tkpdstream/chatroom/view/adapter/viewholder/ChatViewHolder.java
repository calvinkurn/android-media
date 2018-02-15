package com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public class ChatViewHolder extends AbstractViewHolder<ChatViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.chat_view_holder;

    public ChatViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(ChatViewModel element) {

    }
}
