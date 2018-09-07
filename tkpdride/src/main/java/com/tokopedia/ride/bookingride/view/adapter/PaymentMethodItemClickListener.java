package com.tokopedia.ride.bookingride.view.adapter;

import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PaymentMethodViewModel;

/**
 * Created by Vishal Gupta
 */

public interface PaymentMethodItemClickListener {
    void onPaymentMethodSelected(PaymentMethodViewModel paymentMethodViewModel);
}