package com.tokopedia.inbox.inboxchat.viewholder;

import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.View;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachImageModel;

/**
 * Created by stevenfredian on 11/28/17.
 */

public class AttachImageViewHolder extends AbstractViewHolder<AttachImageModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.thumbnail_chat;

    SquareImageView imageView;
    String link;
    String imageUri;
    int gravity;

    public AttachImageViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
    }

    @Override
    public void bind(AttachImageModel element) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ImageHandler.loadImage2(imageView, link, R.drawable.product_no_photo_default);
    }
}
