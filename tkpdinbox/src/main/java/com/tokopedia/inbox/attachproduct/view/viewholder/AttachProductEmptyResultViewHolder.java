package com.tokopedia.inbox.attachproduct.view.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;

/**
 * Created by Hendri on 05/03/18.
 */

public class AttachProductEmptyResultViewHolder extends EmptyResultViewHolder {
    public AttachProductEmptyResultViewHolder(View itemView) {
        super(itemView);
//        this.emptyIconImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    public AttachProductEmptyResultViewHolder(View itemView, Callback callback) {
        super(itemView, callback);
    }
}
