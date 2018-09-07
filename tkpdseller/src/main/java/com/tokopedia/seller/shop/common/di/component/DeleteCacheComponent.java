package com.tokopedia.seller.shop.common.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.shop.common.di.module.ShopDeleteCacheModule;
import com.tokopedia.seller.shop.common.di.scope.DeleteCacheScope;
import com.tokopedia.seller.shopsettings.address.activity.ManageShopAddress;
import com.tokopedia.seller.shopsettings.edit.view.ShopEditorActivity;

import dagger.Component;

/**
 * @author sebastianuskh on 5/8/17.
 */
@DeleteCacheScope
@Component(modules = ShopDeleteCacheModule.class, dependencies = AppComponent.class)
public interface DeleteCacheComponent {

    void inject(ShopEditorActivity shopEditorActivity);

    void inject(ManageShopAddress manageShopAddress);
}
