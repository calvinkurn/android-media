package com.tokopedia.ride.history.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.history.view.adapter.ItemClickListener;
import com.tokopedia.ride.history.view.adapter.viewholder.RideHistoryViewHolder;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

/**
 * Created by alvarisi on 4/11/17.
 */

public class RideHistoryAdapterTypeFactory extends BaseAdapterTypeFactory implements RideHistoryTypeFactory {
    private final ItemClickListener mItemClickListener;

    public RideHistoryAdapterTypeFactory(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int type(RideHistoryViewModel transactionViewModel) {
        return RideHistoryViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;
        if (type == RideHistoryViewHolder.LAYOUT) {
            viewHolder = new RideHistoryViewHolder(parent, mItemClickListener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
