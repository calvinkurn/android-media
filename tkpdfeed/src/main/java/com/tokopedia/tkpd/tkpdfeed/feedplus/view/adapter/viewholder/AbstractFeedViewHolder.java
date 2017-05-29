package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;

/**
 * Created by stevenfredian on 5/26/17.
 */

public abstract class AbstractFeedViewHolder<T> extends AbstractViewHolder<T> {

    public AbstractFeedViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T element, int position);
}
