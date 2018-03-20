package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;


import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.compoundview.HeaderHomeView;

/**
 * @author anggaprasetiyo on 11/12/17.
 */

public class HeaderViewHolder extends AbstractViewHolder<HeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_item_header_home;
    private final HomeCategoryListener listener;
    private LinearLayout mainContainer;

    public HeaderViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.listener = listener;
        mainContainer = itemView.findViewById(R.id.container);
    }

    @Override
    public void bind(HeaderViewModel element) {
        mainContainer.removeAllViews();
        mainContainer.addView(new HeaderHomeView(itemView.getContext(), element, listener), 0);
    }
}
