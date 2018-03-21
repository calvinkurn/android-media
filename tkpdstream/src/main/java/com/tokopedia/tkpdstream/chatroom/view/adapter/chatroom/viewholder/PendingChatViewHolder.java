package com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.PendingChatViewModel;

/**
 * @author by nisie on 2/15/18.
 */

public class PendingChatViewHolder extends AbstractViewHolder<PendingChatViewModel> {

    private TextView message;
    private TextView nickname;
    private ImageView avatar;
    private ImageView influencerBadge;


    @LayoutRes
    public static final int LAYOUT = R.layout.pending_chat_view_holder;

    public PendingChatViewHolder(View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
        nickname = itemView.findViewById(R.id.nickname);
        avatar = itemView.findViewById(R.id.avatar);
        influencerBadge = itemView.findViewById(R.id.influencer_badge);
    }

    @Override
    public void bind(PendingChatViewModel element) {
        ImageHandler.loadImageCircle2(avatar.getContext(), avatar, element.getSenderIconUrl());
        nickname.setText(MethodChecker.fromHtml(element.getSenderName()));
        message.setText(MethodChecker.fromHtml(element.getMessage()));

        if (element.isInfluencer()) {
            influencerBadge.setVisibility(View.VISIBLE);
        } else {
            influencerBadge.setVisibility(View.GONE);
        }
    }
}
