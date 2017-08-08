package com.tokopedia.seller.product.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.seller.product.di.module.YoutubeVideoModule;
import com.tokopedia.seller.product.edit.view.activity.ProductAddActivity;
import com.tokopedia.seller.product.edit.view.activity.YoutubeAddVideoActivity;
import com.tokopedia.seller.product.edit.view.fragment.YoutubeAddVideoFragment;

import dagger.Component;

/**
 * @author normansyahputa on 4/11/17.
 */
@ActivityScope
@Component(modules = YoutubeVideoModule.class, dependencies = AppComponent.class)
public interface YoutubeVideoComponent {
    void inject(ProductAddActivity productAddActivity);

    void inject(YoutubeAddVideoActivity youtubeAddVideoActivity);

    void inject(YoutubeAddVideoFragment youtubeAddVideoFragment);
}
