package com.tokopedia.inbox.inboxchat.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.viewmodel.ImageUploadViewModel;

/**
 * Created by stevenfredian on 11/28/17.
 */

public class ImageUploadViewHolder extends AbstractViewHolder<ImageUploadViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.thumbnail_chat;

    ImageView imageView;
    String link;
    String imageUri;
    int gravity;

    public ImageUploadViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
    }

    @Override
    public void bind(ImageUploadViewModel element) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ImageHandler.loadImage2(imageView, link, R.drawable.product_no_photo_default);
    }
}
