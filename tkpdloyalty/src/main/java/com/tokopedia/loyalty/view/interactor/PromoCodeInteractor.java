package com.tokopedia.loyalty.view.interactor;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCodeInteractor implements IPromoCodeInteractor {

    private final CompositeSubscription compositeSubscription;

    @Inject
    public PromoCodeInteractor(CompositeSubscription compositeSubscription) {
        this.compositeSubscription = compositeSubscription;
    }


}
