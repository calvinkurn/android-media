package com.tokopedia.topads.sdk.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.LoadingViewModel;

/**
 * @author by errysuprayogi on 4/18/17.
 */

public class LoadingViewHolder extends AbstractViewHolder<LoadingViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_loading;

    public LoadingViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LoadingViewModel element) {

    }
}
