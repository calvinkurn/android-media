package com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ImageViewModel;

/**
 * @author by nisie on 2/27/18.
 */

public class ImageViewHolder extends BaseChatViewHolder<ImageViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.image_view_holder;
    private final GroupChatContract.View.ImageViewHolderListener listener;

    private ImageView contentImage;

    public ImageViewHolder(View itemView, GroupChatContract.View.ImageViewHolderListener listener) {
        super(itemView);
        this.listener = listener;
        contentImage = itemView.findViewById(R.id.content_image);
    }

    @Override
    public void bind(final ImageViewModel element) {
        super.bind(element);
        ImageHandler.LoadImage(contentImage, element.getContentImageUrl());
        contentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRedirectUrl(element.getRedirectUrl());
            }
        });
    }
}
