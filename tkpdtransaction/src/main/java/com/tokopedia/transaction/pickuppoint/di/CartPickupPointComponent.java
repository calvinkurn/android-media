package com.tokopedia.transaction.pickuppoint.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.transaction.cart.fragment.CartFragment;
import com.tokopedia.transaction.cart.presenter.CartPresenter;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 05/01/18.
 */

@CartPickupPointScope
@Component(modules = CartPickupPointModule.class, dependencies = AppComponent.class)
public interface CartPickupPointComponent {
    void inject(CartFragment cartFragment);

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

}
