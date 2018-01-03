package com.tokopedia.loyalty.di.module;

import com.tokopedia.loyalty.di.PromoScope;
import com.tokopedia.loyalty.domain.repository.IPromoRepository;
import com.tokopedia.loyalty.view.interactor.IPromoInteractor;
import com.tokopedia.loyalty.view.interactor.PromoInteractor;
import com.tokopedia.loyalty.view.presenter.IPromoListPresenter;
import com.tokopedia.loyalty.view.presenter.PromoListPresenter;
import com.tokopedia.loyalty.view.view.IPromoListView;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 03/01/18.
 */
@Module(includes = {ServiceApiModule.class})
public class PromoListViewModule {

    private final IPromoListView view;

    public PromoListViewModule(IPromoListView view) {
        this.view = view;
    }

    @Provides
    @PromoScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @PromoScope
    IPromoInteractor provideIPromoInteractor(CompositeSubscription compositeSubscription,
                                             IPromoRepository promoRepository) {
        return new PromoInteractor(compositeSubscription, promoRepository);
    }

    @Provides
    @PromoScope
    IPromoListPresenter provideIPromoListPresenter(IPromoInteractor promoInteractor) {
        return new PromoListPresenter(promoInteractor, view);
    }
}
