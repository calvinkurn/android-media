package com.tokopedia.inbox.inboxchat.helper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;

/**
 * Created by Hendri on 15/03/18.
 */

public class ChatGlideImageRequestListener implements RequestListener<String, GlideDrawable> {
    @Override
    public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
        return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
        if (target instanceof GlideDrawableImageViewTarget) {
            ImageView targetImageView = ((GlideDrawableImageViewTarget) target).getView();
            targetImageView.layout(0,0,0,0);
            int width = glideDrawable.getIntrinsicWidth();
            int height = glideDrawable.getIntrinsicHeight();
            if (width > height) {
                if (targetImageView.getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT) {
                    targetImageView.getLayoutParams().width = targetImageView.getMaxWidth();
                }
                targetImageView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else {
                targetImageView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                if(targetImageView.getLayoutParams().height != ViewGroup.LayoutParams.MATCH_PARENT) {
                    targetImageView.getLayoutParams().height = targetImageView.getMaxHeight();
                }
            }
            targetImageView.requestLayout();
        }
        return false;
    }
}
