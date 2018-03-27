package com.tokopedia.loyalty.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.loyalty.di.PromoDetailScope;
import com.tokopedia.loyalty.di.module.PromoDetailModule;
import com.tokopedia.loyalty.view.fragment.PromoDetailFragment;

import dagger.Component;

/**
 * @author Aghny A. Putra on 26/03/18
 */

@PromoDetailScope
@Component(modules = PromoDetailModule.class, dependencies = BaseAppComponent.class)
public interface PromoDetailComponent {

    void inject(PromoDetailFragment promoDetailFragment);

}
