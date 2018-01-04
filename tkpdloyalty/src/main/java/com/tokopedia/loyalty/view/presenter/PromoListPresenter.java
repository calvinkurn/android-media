package com.tokopedia.loyalty.view.presenter;

import com.tokopedia.loyalty.view.interactor.IPromoInteractor;
import com.tokopedia.loyalty.view.view.IPromoListView;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 02/01/18.
 */

public class PromoListPresenter implements IPromoListPresenter {
    private final IPromoInteractor promoInteractor;
    private final IPromoListView view;

    @Inject
    public PromoListPresenter(IPromoInteractor promoInteractor, IPromoListView view) {
        this.promoInteractor = promoInteractor;
        this.view = view;
    }


    @Override
    public void processGetPromoList(String... subCategories) {

    }
}
