package com.tokopedia.tkpdstream.channel.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;

/**
 * @author by StevenFredian on 13/02/18.
 */


public class ChannelViewHolder extends AbstractViewHolder<ChannelViewModel>{

    @LayoutRes
    public static final int LAYOUT = R.layout.card_group_chat;
    private final ImageView image;
    private final ImageView profile;
    private final TextView title;
    private final TextView subtitle;
    private final TextView name;
    private final TextView participant;

    public ChannelViewHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.product_image);
        profile = itemView.findViewById(R.id.prof_pict);
        title = itemView.findViewById(R.id.title);
        subtitle = itemView.findViewById(R.id.subtitle);
        name = itemView.findViewById(R.id.name);
        participant = itemView.findViewById(R.id.participant);

    }

    @Override
    public void bind(ChannelViewModel element) {

        participant.setText(String.valueOf(element.getParticipant()));
        name.setText(element.getName());
        title.setText(element.getTitle());
        subtitle.setText(element.getSubtitle());

        ImageHandler.loadImage2(image, element.getImage(), R.drawable.loading_page);
        ImageHandler.loadImageCircle2(profile.getContext(), profile, element.getProfile(), R.drawable.loading_page);

    }
}
