package com.tokopedia.ride.history.di;

import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.history.view.RideHistoryDetailFragment;
import com.tokopedia.ride.history.view.RideHistoryFragment;

import dagger.Component;

/**
 * Created by alvarisi on 7/26/17.
 */
@RideHistoryScope
@Component(modules = RideHistoryModule.class, dependencies = RideComponent.class)
public interface RideHistoryComponent {
    void inject(RideHistoryDetailFragment rideHistoryDetailFragment);

    void inject(RideHistoryFragment rideHistoryFragment);
}
