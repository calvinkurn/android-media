package com.tokopedia.tkpdtrain.homepage.di;

import com.tokopedia.tkpdtrain.common.di.TrainComponent;
import com.tokopedia.tkpdtrain.homepage.presentation.fragment.TrainHomepageFragment;

import dagger.Component;

/**
 * @author  by alvarisi on 3/1/18.
 */
@TrainHomepageScope
@Component(modules = TrainHomepageModule.class, dependencies = TrainComponent.class)
public interface TrainHomepageComponent {
    void inject(TrainHomepageFragment trainHomepageFragment);
}
