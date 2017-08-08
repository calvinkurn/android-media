package com.tokopedia.ride.ontrip.di;

import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.completetrip.view.CompleteTripFragment;
import com.tokopedia.ride.ontrip.view.SendCancelReasonActivity;
import com.tokopedia.ride.ontrip.view.fragment.OnTripMapFragment;

import dagger.Component;

/**
 * Created by alvarisi on 7/26/17.
 */
@OnTripScope
@Component(modules = OnTripModule.class, dependencies = RideComponent.class)
public interface OnTripComponent {
    void inject(OnTripMapFragment onTripMapFragment);

    void inject(SendCancelReasonActivity sendCancelReasonActivity);

}
