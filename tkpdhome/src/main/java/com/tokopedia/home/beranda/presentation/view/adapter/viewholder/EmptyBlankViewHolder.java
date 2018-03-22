package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;

/**
 * Created by meta on 22/03/18.
 */

public class EmptyBlankViewHolder extends AbstractViewHolder<EmptyBlankViewHolder> {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_blank;

    public EmptyBlankViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(EmptyBlankViewHolder element) {
    }
}
