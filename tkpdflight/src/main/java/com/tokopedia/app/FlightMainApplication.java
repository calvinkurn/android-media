package com.tokopedia.app;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdFlightGeneratedDatabaseHolder;
import com.tokopedia.abstraction.base.app.BaseMainApplication;

/**
 * TODO
 * THIS IS JUST TO MOCKUP FLIGHT APP
 * CHANGE TO CONSUMER APP LATER
 */
public class FlightMainApplication extends BaseMainApplication{

    @Override
    protected void initDbFlow() {
        super.initDbFlow();
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdFlightGeneratedDatabaseHolder.class)
                .build());
    }

}
