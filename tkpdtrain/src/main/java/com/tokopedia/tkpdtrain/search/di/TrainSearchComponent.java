package com.tokopedia.tkpdtrain.search.di;

import com.tokopedia.tkpdtrain.common.di.TrainComponent;
import com.tokopedia.tkpdtrain.station.di.TrainStationsScope;

import dagger.Component;

/**
 * Created by nabilla on 3/9/18.
 */
@TrainStationsScope
@Component(modules = TrainSearchModule.class, dependencies = TrainComponent.class)
public interface TrainSearchComponent {
}
