package com.tokopedia.loyalty.view.presenter;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.interactor.IPromoInteractor;
import com.tokopedia.loyalty.view.view.IPromoDetailView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class PromoDetailPresenter extends IBasePresenter<IPromoDetailView> {
    private final IPromoInteractor promoInteractor;

    @Inject
    public PromoDetailPresenter(IPromoInteractor promoInteractor) {
        this.promoInteractor = promoInteractor;
    }

    public void getPromoDetail(String slug) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("slug", slug);
        this.promoInteractor.getPromoList(param, new Subscriber<List<PromoData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<PromoData> promoData) {
                getMvpView().renderPromoDetail(promoData.get(0));
            }
        });
    }

}
