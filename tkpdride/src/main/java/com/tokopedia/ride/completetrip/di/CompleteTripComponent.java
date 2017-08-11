package com.tokopedia.ride.completetrip.di;

import com.tokopedia.ride.common.ride.di.RideComponent;
import com.tokopedia.ride.completetrip.view.CompleteTripFragment;

import dagger.Component;

/**
 * Created by alvarisi on 7/26/17.
 */
@CompleteTripScope
@Component(modules = CompleteTripModule.class, dependencies = RideComponent.class)
public interface CompleteTripComponent {
    void inject(CompleteTripFragment completeTripFragment);
}
