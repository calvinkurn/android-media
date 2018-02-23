package com.tokopedia.shop.info.di.component;

import com.tokopedia.shop.address.view.fragment.ShopAddressListFragment;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.info.di.module.ShopInfoModule;
import com.tokopedia.shop.info.di.scope.ShopInfoScope;
import com.tokopedia.shop.page.view.activity.ShopPageActivity;
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@ShopInfoScope
@Component(modules = ShopInfoModule.class, dependencies = ShopComponent.class)
public interface ShopInfoComponent {

    void inject(ShopInfoFragment shopInfoDetailFragment);

    void inject(ShopAddressListFragment shopAddressListFragment);

}
