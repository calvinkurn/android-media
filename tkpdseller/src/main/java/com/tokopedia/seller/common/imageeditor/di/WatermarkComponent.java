package com.tokopedia.seller.common.imageeditor.di;

import com.tokopedia.seller.common.imageeditor.ImageEditorWatermarkFragment;
import com.tokopedia.seller.common.imageeditor.di.scope.WatermarkScope;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;

import dagger.Component;

/**
 * @author sebastianuskh on 5/8/17.
 */
@WatermarkScope
@Component(dependencies = ShopComponent.class)
public interface WatermarkComponent {
    void inject(ImageEditorWatermarkFragment imageEditorWatermarkFragment);
}
