package com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.viewholder;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.BaseChatViewModel;

/**
 * @author by nisie on 2/27/18.
 */

abstract class BaseChatViewHolder<T extends Visitable> extends AbstractViewHolder<T> {

    protected TextView nickname;
    protected TextView postTime;
    protected ImageView avatar;
    protected TextView adminLabel;
    protected ImageView influencerBadge;
    protected TextView headerTime;

    public BaseChatViewHolder(View itemView) {
        super(itemView);
        nickname = itemView.findViewById(R.id.nickname);
        postTime = itemView.findViewById(R.id.post_time);
        avatar = itemView.findViewById(R.id.avatar);
        adminLabel = itemView.findViewById(R.id.label);
        influencerBadge = itemView.findViewById(R.id.influencer_badge);
        headerTime = itemView.findViewById(R.id.header_time);
    }

    @Override
    public void bind(T element) {
        if (element instanceof BaseChatViewModel) {
            BaseChatViewModel viewModel = (BaseChatViewModel) element;

            ImageHandler.loadImageCircle2(avatar.getContext(), avatar, viewModel.getSenderIconUrl());
            nickname.setText(MethodChecker.fromHtml(viewModel.getSenderName()));
            postTime.setText(viewModel.getFormattedUpdatedAt());

            if (viewModel.isAdministrator()) {
                nickname.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                nickname.setTextColor(MethodChecker.getColor(nickname.getContext(), R.color.medium_green));
                adminLabel.setVisibility(View.VISIBLE);
            } else {
                nickname.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
                nickname.setTextColor(MethodChecker.getColor(nickname.getContext(), R.color.font_black_disabled_38));
                adminLabel.setVisibility(View.GONE);
            }

            if (viewModel.isInfluencer()) {
                influencerBadge.setVisibility(View.VISIBLE);
            } else {
                influencerBadge.setVisibility(View.GONE);
            }

            if (viewModel.isShowHeaderTime()) {
                headerTime.setVisibility(View.VISIBLE);
                headerTime.setText(viewModel.getFormattedHeaderTime());
            } else {
                headerTime.setVisibility(View.GONE);
            }
        }
    }
}
