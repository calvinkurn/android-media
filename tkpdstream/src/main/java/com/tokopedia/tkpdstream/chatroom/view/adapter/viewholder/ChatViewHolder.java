package com.tokopedia.tkpdstream.chatroom.view.adapter.viewholder;

import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public class ChatViewHolder extends AbstractViewHolder<ChatViewModel> {

    private TextView message;
    private TextView nickname;
    private TextView postTime;
    private ImageView avatar;
    private TextView adminLabel;
    private ImageView influencerBadge;

    @LayoutRes
    public static final int LAYOUT = R.layout.chat_view_holder;

    public ChatViewHolder(View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
        nickname = itemView.findViewById(R.id.nickname);
        postTime = itemView.findViewById(R.id.post_time);
        avatar = itemView.findViewById(R.id.avatar);
        adminLabel = itemView.findViewById(R.id.label);
        influencerBadge = itemView.findViewById(R.id.influencer_badge);

    }

    @Override
    public void bind(ChatViewModel element) {
        ImageHandler.loadImageCircle2(avatar.getContext(), avatar, element.getSenderIconUrl());
        nickname.setText(MethodChecker.fromHtml(element.getSenderName()));
        postTime.setText(element.getCreatedAt());
        message.setText(MethodChecker.fromHtml(element.getMessage()));

        if (element.isAdministrator()) {
            nickname.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            nickname.setTextColor(MethodChecker.getColor(nickname.getContext(), R.color.medium_green));
            adminLabel.setVisibility(View.VISIBLE);
        } else {
            nickname.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            nickname.setTextColor(MethodChecker.getColor(nickname.getContext(), R.color.font_black_disabled_38));
            adminLabel.setVisibility(View.GONE);
        }

        if (element.isInfluencer()) {
            influencerBadge.setVisibility(View.VISIBLE);
        } else {
            influencerBadge.setVisibility(View.GONE);
        }
    }
}
