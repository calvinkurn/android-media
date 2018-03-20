package com.tokopedia.tkpdtrain.search.di;

import com.tokopedia.tkpdtrain.common.di.TrainComponent;
import com.tokopedia.tkpdtrain.search.presentation.fragment.TrainReturnSearchFragment;
import com.tokopedia.tkpdtrain.search.presentation.fragment.TrainSearchFragment;

import dagger.Component;

/**
 * Created by nabilla on 3/9/18.
 */
@TrainSearchScope
@Component(modules = TrainSearchModule.class, dependencies = TrainComponent.class)
public interface TrainSearchComponent {
    void inject(TrainReturnSearchFragment trainReturnSearchFragment);
    void inject(TrainSearchFragment trainSearchFragment);
}
