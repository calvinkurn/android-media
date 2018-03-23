package com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.listener.ChatroomContract;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ImageAnnouncementViewModel;

/**
 * @author by nisie on 2/27/18.
 */

public class ImageAnnouncementViewHolder extends BaseChatViewHolder<ImageAnnouncementViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.image_view_holder;
    private final ChatroomContract.View.ImageAnnouncementViewHolderListener listener;

    private ImageView contentImage;

    public ImageAnnouncementViewHolder(View itemView, ChatroomContract.View.ImageAnnouncementViewHolderListener listener) {
        super(itemView);
        this.listener = listener;
        contentImage = itemView.findViewById(R.id.content_image);
    }

    @Override
    public void bind(final ImageAnnouncementViewModel element) {
        super.bind(element);
        ImageHandler.LoadImage(contentImage, element.getContentImageUrl());
        contentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImageAnnouncementClicked(element.getRedirectUrl());
            }
        });
    }
}
