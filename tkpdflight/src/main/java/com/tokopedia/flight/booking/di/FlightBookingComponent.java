package com.tokopedia.flight.booking.di;

import com.tokopedia.flight.booking.view.fragment.FLightBookingPhoneCodeFragment;
import com.tokopedia.flight.booking.view.fragment.FlightBookingFragment;
import com.tokopedia.flight.booking.view.fragment.FlightBookingNationalityFragment;
import com.tokopedia.flight.booking.view.fragment.FlightBookingPassengerFragment;
import com.tokopedia.flight.booking.view.fragment.FlightBookingListPassengerFragment;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.review.view.fragment.FlightBookingReviewFragment;

import dagger.Component;

/**
 * Created by alvarisi on 11/8/17.
 */
@FlightBookingScope
@Component(modules = FlightBookingModule.class, dependencies = FlightComponent.class)
public interface FlightBookingComponent {
    void inject(FlightBookingFragment flightBookingFragment);

    void inject(FLightBookingPhoneCodeFragment fLightBookingPhoneCodeFragment);

    void inject(FlightBookingNationalityFragment flightBookingNationalityFragment);

    void inject(FlightBookingReviewFragment flightBookingReviewFragment);

    void inject(FlightBookingPassengerFragment flightBookingPassengerFragment);

    void inject(FlightBookingListPassengerFragment flightBookingListPassengerFragment);

}
