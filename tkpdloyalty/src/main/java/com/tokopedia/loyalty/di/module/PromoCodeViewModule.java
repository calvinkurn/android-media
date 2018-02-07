package com.tokopedia.loyalty.di.module;

import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.loyalty.domain.repository.TokoPointRepository;
import com.tokopedia.loyalty.view.interactor.IPromoCodeInteractor;
import com.tokopedia.loyalty.view.interactor.PromoCodeInteractor;
import com.tokopedia.loyalty.view.presenter.IPromoCodePresenter;
import com.tokopedia.loyalty.view.presenter.PromoCodePresenter;
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

    @Provides
    @LoyaltyScope
    IPromoCodeInteractor providePromoCodeInteractor(CompositeSubscription compositeSubscription,
                                                    TokoPointRepository loyaltyRepository) {
        return new PromoCodeInteractor(compositeSubscription, loyaltyRepository);
    }

    @Provides
    @LoyaltyScope
    IPromoCodePresenter provideIPromoCodePresenter(IPromoCodeInteractor promoCodeInteractor) {
        return new PromoCodePresenter(view, promoCodeInteractor);
    }
}
