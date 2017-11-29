package com.tokopedia.loyalty.view.presenter;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.interactor.IPromoCouponInteractor;
import com.tokopedia.loyalty.view.view.IPromoCouponView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCouponPresenter implements IPromoCouponPresenter {

    private final IPromoCouponInteractor promoCouponInteractor;
    private final IPromoCouponView view;

    @Inject
    public PromoCouponPresenter(IPromoCouponView view, IPromoCouponInteractor promoCouponInteractor) {
        this.view = view;
        this.promoCouponInteractor = promoCouponInteractor;
    }

    @Override
    public void processGetCouponList() {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("param1", "value1");
        param.put("param2", "value2");
        promoCouponInteractor.getCouponList(
                param,
                new Subscriber<List<CouponData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<CouponData> couponData) {
                        view.renderCouponListDataResult(couponData);
                    }
                });
    }

    @Override
    public void processPostCouponValidateRedeem() {

    }

    @Override
    public void processPostCouponRedeem() {

    }

    @Override
    public void processGetPointRecentHistory() {

    }

    @Override
    public void processGetCatalogList() {

    }

    @Override
    public void processGetCatalogDetail() {

    }

    @Override
    public void processGetCatalogFilterCategory() {

    }
}
