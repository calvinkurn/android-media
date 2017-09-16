package com.tokopedia.gm.featured.di.component;

/**
 * Created by normansyahputa on 9/7/17.
 */

import com.tokopedia.gm.common.di.component.GoldMerchantComponent;
import com.tokopedia.gm.featured.di.module.FeaturedProductModule;
import com.tokopedia.gm.featured.di.scope.FeaturedProductScope;
import com.tokopedia.gm.featured.view.activity.FeaturedProductActivity;
import com.tokopedia.gm.featured.view.fragment.FeaturedProductFragment;

import dagger.Component;

/**
 * Created by normansyahputa on 7/6/17.
 */
@FeaturedProductScope
@Component(modules = FeaturedProductModule.class, dependencies = GoldMerchantComponent.class)
public interface FeaturedProductComponent {
    void inject(FeaturedProductActivity featuredProductActivity);

    void inject(FeaturedProductFragment featuredProductFragment);
}
