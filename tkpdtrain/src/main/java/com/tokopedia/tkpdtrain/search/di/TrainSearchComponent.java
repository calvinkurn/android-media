package com.tokopedia.tkpdtrain.search.di;

import com.tokopedia.tkpdtrain.common.di.TrainComponent;
import com.tokopedia.tkpdtrain.search.presentation.TrainSearchFragment;

import dagger.Component;

/**
 * Created by nabilla on 3/9/18.
 */
@TrainSearchScope
@Component(modules = TrainSearchModule.class, dependencies = TrainComponent.class)
public interface TrainSearchComponent {
    void inject(TrainSearchFragment trainSearchFragment);
}
