package com.tokopedia.ride.bookingride.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.bookingride.view.adapter.ItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.viewholder.PlaceAutoCompleteViewHolder;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PlaceAutoCompeleteViewModel;

/**
 * Created by alvarisi on 3/15/17.
 */

public class PlaceAutoCompleteAdapterTypeFactory extends BaseAdapterTypeFactory implements PlaceAutoCompleteTypeFactory {
    private final ItemClickListener mItemClickListener;

    public PlaceAutoCompleteAdapterTypeFactory(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public int type(PlaceAutoCompeleteViewModel placeAutoCompeleteViewModel) {
        return PlaceAutoCompleteViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;
        if (type == PlaceAutoCompleteViewHolder.LAYOUT) {
            viewHolder = new PlaceAutoCompleteViewHolder(parent, mItemClickListener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
