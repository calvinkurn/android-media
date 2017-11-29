package com.tokopedia.loyalty.view.presenter;

import com.tokopedia.loyalty.view.view.IPromoCodeView;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public class PromoCodePresenter implements IPromoCodePresenter {
    private final IPromoCodeView view;

    @Inject
    public PromoCodePresenter(IPromoCodeView view) {
        this.view = view;
    }

    @Override
    public void processCheckPromoCode() {

    }
}
