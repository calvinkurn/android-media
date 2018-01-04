package com.tokopedia.seller.seller.info.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.seller.info.di.module.SellerInfoModule;
import com.tokopedia.seller.seller.info.di.scope.SellerInfoScope;
import com.tokopedia.seller.seller.info.view.fragment.SellerInfoFragment;

import dagger.Component;

/**
 * Created by normansyahputa on 11/30/17.
 */
@SellerInfoScope
@Component(modules = SellerInfoModule.class, dependencies = AppComponent.class)
public interface SellerInfoComponent {
    void inject(SellerInfoFragment sellerInfoFragment);
}
