package com.tokopedia.trainapp;

import com.facebook.stetho.Stetho;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.train.common.util.TrainDatabase;

/**
 * Created by alvarisi on 3/8/18.
 */

public class TrainAppApplication extends BaseMainApplication {
    public TrainAppApplication() {
    }

    @Override
    public void onCreate() {
        TrainDatabase.init(this);
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
