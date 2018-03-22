package com.tokopedia.seller.product.edit.di.component;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.di.module.YoutubeVideoModule;
import com.tokopedia.seller.product.edit.view.activity.ProductAddActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductAddVideoActivity;
import com.tokopedia.seller.product.edit.view.fragment.ProductAddVideoFragment;

import dagger.Component;

/**
 * @author normansyahputa on 4/11/17.
 */
@ActivityScope
@Component(modules = YoutubeVideoModule.class, dependencies = ProductComponent.class)
public interface YoutubeVideoComponent {
    void inject(ProductAddActivity productAddActivity);

    void inject(ProductAddVideoActivity youtubeAddVideoActivity);

    void inject(ProductAddVideoFragment youtubeAddVideoFragment);
}
