package com.tokopedia.ride.bookingride.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;

/**
 * Created by Vishal on 27th Sep, 2017.
 */

public interface PaymentMethodTypeFactory {
    int type(PaymentMethodViewModel paymentMethodViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
