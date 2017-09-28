package com.tokopedia.ride.bookingride.di;

import com.tokopedia.ride.bookingride.di.scope.BookingRideScope;
import com.tokopedia.ride.bookingride.view.activity.AddCreditCardActivity;
import com.tokopedia.ride.bookingride.view.activity.RideHomeActivity;
import com.tokopedia.ride.bookingride.view.fragment.AddCreditCardFragment;
import com.tokopedia.ride.bookingride.view.fragment.ApplyPromoFragment;
import com.tokopedia.ride.bookingride.view.fragment.ConfirmBookingRideFragment;
import com.tokopedia.ride.bookingride.view.fragment.ManagePaymentOptionsFragment;
import com.tokopedia.ride.bookingride.view.fragment.PlaceAutocompleteFragment;
import com.tokopedia.ride.bookingride.view.fragment.RideHomeMapFragment;
import com.tokopedia.ride.bookingride.view.fragment.UberProductFragment;
import com.tokopedia.ride.common.ride.di.RideComponent;

import dagger.Component;

/**
 * Created by alvarisi on 7/25/17.
 */
@BookingRideScope
@Component(modules = BookingRideModule.class, dependencies = RideComponent.class)
public interface BookingRideComponent {
    void inject(RideHomeActivity activity);

    void inject(RideHomeMapFragment rideHomeMapFragment);

    void inject(UberProductFragment uberProductFragment);

    void inject(ConfirmBookingRideFragment confirmBookingRideFragment);

    void inject(ApplyPromoFragment applyPromoFragment);

    void inject(PlaceAutocompleteFragment placeAutocompleteFragment);

    void inject(AddCreditCardFragment addCreditCardFragment);

    void inject(ManagePaymentOptionsFragment managePaymentOptionsFragment);
}
