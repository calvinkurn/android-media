package com.tokopedia.ride.bookingride.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.bookingride.view.adapter.RideProductItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.viewholder.RideProductViewHolder;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;

/**
 * Created by alvarisi on 3/16/17.
 */

public class RideProductAdapterTypeFactory  extends BaseAdapterTypeFactory implements RideProductTypeFactory {
    private final RideProductItemClickListener mItemClickListener;

    public RideProductAdapterTypeFactory(RideProductItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public int type(RideProductViewModel rideProductViewModel) {
        return RideProductViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;
        if (type == RideProductViewHolder.LAYOUT) {
            viewHolder = new RideProductViewHolder(parent, mItemClickListener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
