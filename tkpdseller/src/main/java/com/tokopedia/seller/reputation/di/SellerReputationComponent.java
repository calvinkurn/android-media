package com.tokopedia.seller.reputation.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.manage.di.ProductManageModule;
import com.tokopedia.seller.product.manage.di.ProductManageScope;
import com.tokopedia.seller.reputation.domain.interactor.SpeedReputationUseCase;
import com.tokopedia.seller.reputation.view.fragment.SellerReputationFragment;

import dagger.Component;

/**
 * Created by normansyahputa on 2/13/18.
 */
@SellerReputationScope
@Component(modules = SellerReputationModule.class, dependencies = AppComponent.class)
public interface SellerReputationComponent {
    void inject(SellerReputationFragment sellerReputationFragment);

    SpeedReputationUseCase speedReputationUseCase();
}
