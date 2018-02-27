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
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ImageViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.UserActionViewModel;

/**
 * @author by nisie on 2/27/18.
 */

public class ImageViewHolder extends AbstractViewHolder<ImageViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.image_view_holder;

    private ImageView contentImage;
    private TextView nickname;
    private TextView postTime;
    private ImageView avatar;
    private TextView adminLabel;
    private ImageView influencerBadge;
    private TextView headerTime;

    public ImageViewHolder(View itemView) {
        super(itemView);
        contentImage = itemView.findViewById(R.id.content_image);
        nickname = itemView.findViewById(R.id.nickname);
        postTime = itemView.findViewById(R.id.post_time);
        avatar = itemView.findViewById(R.id.avatar);
        adminLabel = itemView.findViewById(R.id.label);
        influencerBadge = itemView.findViewById(R.id.influencer_badge);
        headerTime = itemView.findViewById(R.id.header_time);
    }

    @Override
    public void bind(ImageViewModel element) {
        ImageHandler.loadImageCircle2(avatar.getContext(), avatar, element.getSenderIconUrl());
        nickname.setText(MethodChecker.fromHtml(element.getSenderName()));
        postTime.setText(element.getFormattedUpdatedAt());

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

        if (element.isShowHeaderTime()) {
            headerTime.setVisibility(View.VISIBLE);
            headerTime.setText(element.getFormattedHeaderTime());
        } else {
            headerTime.setVisibility(View.GONE);
        }

        ImageHandler.LoadImage(contentImage, element.getContentImageUrl());

    }
}
