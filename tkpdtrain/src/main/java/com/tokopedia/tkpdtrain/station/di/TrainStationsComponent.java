package com.tokopedia.tkpdtrain.station.di;

import com.tokopedia.tkpdtrain.common.di.TrainComponent;
import com.tokopedia.tkpdtrain.common.di.TrainModule;
import com.tokopedia.tkpdtrain.station.presentation.TrainStationsFragment;

import dagger.Component;

/**
 * Created by alvarisi on 3/5/18.
 */
@TrainStationsScope
@Component(modules = TrainStationsModule.class, dependencies = TrainComponent.class)
public interface TrainStationsComponent {
    void inject(TrainStationsFragment trainStationsFragment);
}
