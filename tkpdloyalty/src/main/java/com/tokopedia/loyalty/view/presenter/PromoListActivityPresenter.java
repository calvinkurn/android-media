package com.tokopedia.loyalty.view.presenter;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.loyalty.view.interactor.IPromoInteractor;
import com.tokopedia.loyalty.view.view.IPromoListActivityView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 04/01/18.
 */

public class PromoListActivityPresenter implements IPromoListActivityPresenter {

    private final IPromoInteractor promoInteractor;
    private final IPromoListActivityView view;

    @Inject
    public PromoListActivityPresenter(IPromoInteractor promoInteractor, IPromoListActivityView view) {
        this.promoInteractor = promoInteractor;
        this.view = view;
    }

    @Override
    public void processGetPromoMenu() {
        promoInteractor.getPromoMenuList(
                new TKPDMapParam<String, String>(), new Subscriber<List<PromoMenuData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<PromoMenuData> promoMenuData) {
                        view.renderPromoMenuDataList(promoMenuData);
                    }
                }
        );
    }
}
