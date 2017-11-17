package com.tokopedia.seller.common.imageeditor.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.common.imageeditor.ImageEditorWatermarkFragment;
import com.tokopedia.seller.common.imageeditor.module.GetShopInfoModule;
import com.tokopedia.seller.common.imageeditor.scope.GetShopInfoScope;
import com.tokopedia.seller.shop.ShopEditorActivity;
import com.tokopedia.seller.shop.di.scope.DeleteCacheScope;
import com.tokopedia.seller.shopsettings.address.activity.ManageShopAddress;

import dagger.Component;

/**
 * @author sebastianuskh on 5/8/17.
 */
@GetShopInfoScope
@Component(modules = GetShopInfoModule.class,dependencies = AppComponent.class)
public interface GetShopInfoComponent {
    void inject(ImageEditorWatermarkFragment imageEditorWatermarkFragment);
}
