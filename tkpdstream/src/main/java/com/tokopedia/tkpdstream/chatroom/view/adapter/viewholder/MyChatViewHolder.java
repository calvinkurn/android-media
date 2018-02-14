package com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.MyChatViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public class MyChatViewHolder extends AbstractViewHolder<MyChatViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.my_chat_view_holder;

    public MyChatViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(MyChatViewModel element) {

    }
}
