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

public class ImageViewHolder extends BaseChatViewHolder<ImageViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.image_view_holder;

    private ImageView contentImage;

    public ImageViewHolder(View itemView) {
        super(itemView);
        contentImage = itemView.findViewById(R.id.content_image);
    }

    @Override
    public void bind(ImageViewModel element) {
        super.bind(element);
        ImageHandler.LoadImage(contentImage, element.getContentImageUrl());

    }
}
