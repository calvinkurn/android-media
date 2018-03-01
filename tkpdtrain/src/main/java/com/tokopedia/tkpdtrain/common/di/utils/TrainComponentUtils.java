package com.tokopedia.tkpdtrain.common.di.utils;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.tkpdtrain.common.di.DaggerTrainComponent;
import com.tokopedia.tkpdtrain.common.di.TrainComponent;

/**
 * @author  by alvarisi on 3/1/18.
 */

public class TrainComponentUtils {
    private static TrainComponent trainComponent;

    public static TrainComponent getTrainComponent(Application application) {
        if (trainComponent == null) {
            trainComponent = DaggerTrainComponent.builder().baseAppComponent(
                    ((BaseMainApplication) application).getBaseAppComponent()).build();
        }
        return trainComponent;
    }
}
