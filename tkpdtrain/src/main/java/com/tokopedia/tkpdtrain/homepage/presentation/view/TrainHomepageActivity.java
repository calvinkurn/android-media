package com.tokopedia.tkpdtrain.homepage.presentation.view;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tkpdtrain.common.di.utils.TrainComponentUtils;
import com.tokopedia.tkpdtrain.common.presentation.TrainBaseActivity;
import com.tokopedia.tkpdtrain.homepage.di.DaggerTrainHomepageComponent;
import com.tokopedia.tkpdtrain.homepage.di.TrainHomepageComponent;


public class TrainHomepageActivity extends TrainBaseActivity implements HasComponent<TrainHomepageComponent> {
    private static TrainHomepageComponent component;

    @Override
    protected Fragment getNewFragment() {
        return new TrainHomepageFragment();
    }

    @Override
    public TrainHomepageComponent getComponent() {
        if (component == null){
            component = DaggerTrainHomepageComponent.builder()
                    .trainComponent(TrainComponentUtils.getTrainComponent(getApplication()))
                    .build();
        }
        return component;
    }
}
