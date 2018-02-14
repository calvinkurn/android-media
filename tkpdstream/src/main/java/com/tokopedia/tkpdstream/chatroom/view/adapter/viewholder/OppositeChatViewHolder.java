package com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.OppositeChatViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public class OppositeChatViewHolder extends AbstractViewHolder<OppositeChatViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.opposite_chat_view_holder;

    public OppositeChatViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(OppositeChatViewModel element) {

    }
}
