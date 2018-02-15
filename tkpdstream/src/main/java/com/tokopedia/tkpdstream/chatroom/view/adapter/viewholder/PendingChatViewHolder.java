package com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.PendingChatViewModel;

/**
 * @author by nisie on 2/15/18.
 */

public class PendingChatViewHolder extends AbstractViewHolder<PendingChatViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.pending_chat_view_holder;

    public PendingChatViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(PendingChatViewModel element) {

    }
}
