package com.tokopedia.shop.product.di.component;

import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.di.module.ShopProductModule;
import com.tokopedia.shop.product.di.scope.ShopProductScope;
import com.tokopedia.shop.product.view.fragment.ShopProductListFragment;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopProductScope
@Component(modules = ShopProductModule.class, dependencies = ShopComponent.class)
public interface ShopProductComponent {

    void inject(ShopProductListFragment shopProductListFragment);

}
