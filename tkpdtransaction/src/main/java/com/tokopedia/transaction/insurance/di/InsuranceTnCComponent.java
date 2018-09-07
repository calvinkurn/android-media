package com.tokopedia.transaction.insurance.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.transaction.insurance.view.InsuranceTnCFragment;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

@InsuranceTnCScope
@Component(modules = InsuranceTnCModule.class, dependencies = AppComponent.class)
public interface InsuranceTnCComponent {
    void inject(InsuranceTnCFragment insuranceTnCFragment);

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();
}
