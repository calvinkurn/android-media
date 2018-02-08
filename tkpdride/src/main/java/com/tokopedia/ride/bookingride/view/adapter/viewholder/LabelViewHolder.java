package com.tokopedia.ride.bookingride.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.LabelViewModel;

/**
 * Created by alvarisi on 5/3/17.
 */

public class LabelViewHolder extends AbstractViewHolder<LabelViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.place_auto_complete_saved_list;

    public LabelViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(LabelViewModel element) {

    }
}
