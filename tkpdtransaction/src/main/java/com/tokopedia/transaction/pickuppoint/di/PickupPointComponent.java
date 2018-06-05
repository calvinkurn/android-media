package com.tokopedia.transaction.pickuppoint.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.transaction.pickuppoint.view.activity.PickupPointActivity;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

@PickupPointScope
@Component(modules = PickupPointModule.class, dependencies = AppComponent.class)
public interface PickupPointComponent {
    void inject(PickupPointActivity pickupPointActivity);

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

}