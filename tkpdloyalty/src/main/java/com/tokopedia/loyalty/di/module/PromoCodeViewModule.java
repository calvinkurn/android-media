package com.tokopedia.loyalty.di.module;

import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.view.view.IPromoCodeView;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

@Module(includes = {ServiceApiModule.class})
public class PromoCodeViewModule {

    private final IPromoCodeView view;

    public PromoCodeViewModule(IPromoCodeView view) {
        this.view = view;
    }

    @Provides
    @LoyaltyScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }
}
