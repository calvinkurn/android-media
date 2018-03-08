package com.tokopedia.tkpdtrain.common.util;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.GeneratedDatabaseHolder;

/**
 * Created by alvarisi on 3/8/18.
 */

public class TrainDatabase {
    public static void init(Context applicationContext){
        try{
            FlowManager.getConfig();
        } catch (IllegalStateException e) {
            FlowManager.init(new FlowConfig.Builder(applicationContext).build());
        }
        FlowManager.initModule(GeneratedDatabaseHolder.class);

    }

}
