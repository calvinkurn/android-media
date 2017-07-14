package com.tokopedia.ride.bookingride.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;

/**
 * Created by alvarisi on 3/16/17.
 */

public interface RideProductTypeFactory {
    int type(RideProductViewModel rideProductViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
