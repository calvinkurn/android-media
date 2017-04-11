package com.tokopedia.topads.sdk.base.adapter;

import android.support.annotation.LayoutRes;

import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractAdsViewHolder;

/**
 * @author by errysuprayogi on 4/10/17.
 */

public class TopAdsLayoutWrapper {
    Class model;
    Class<? extends AbstractAdsViewHolder> viewHolder;
    @LayoutRes
    Integer layoutId;
    Integer layoutType;

    public TopAdsLayoutWrapper(Class model, Class<? extends AbstractAdsViewHolder> viewHolder,
                               @LayoutRes Integer layoutId,
                               Integer layoutType) {
        this.model = model;
        this.viewHolder = viewHolder;
        this.layoutId = layoutId;
        this.layoutType = layoutType;
    }

    public Class getModel() {
        return model;
    }

    public Class<? extends AbstractAdsViewHolder> getViewHolder() {
        return viewHolder;
    }

    public Integer getLayoutId() {
        return layoutId;
    }

    public Integer getLayoutType() {
        return layoutType;
    }
}
