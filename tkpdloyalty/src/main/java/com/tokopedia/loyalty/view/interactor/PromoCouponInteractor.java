package com.tokopedia.loyalty.view.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyCouponRedeem;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyValidateRedeem;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.loyalty.view.data.CouponData;
import com.tokopedia.loyalty.view.data.CouponViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCouponInteractor implements IPromoCouponInteractor {
    private final CompositeSubscription compositeSubscription;
    private final ITokoPointRepository tokoplusRepository;

    @Inject
    public PromoCouponInteractor(CompositeSubscription compositeSubscription,
                                 ITokoPointRepository tokoplusRepository) {
        this.compositeSubscription = compositeSubscription;
        this.tokoplusRepository = tokoplusRepository;
    }


    @Override
    public void getCouponList(TKPDMapParam<String, String> param,
                              Subscriber<List<CouponData>> subscriber) {
        compositeSubscription.add(
                tokoplusRepository.getCouponList(param)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void submitVoucher(String couponTitle,
                              String couponCode,
                              TKPDMapParam<String, String> param,
                              Subscriber<CouponViewModel> subscriber) {
        compositeSubscription.add(
                tokoplusRepository.checkCouponValidity(param, couponCode, couponTitle)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void submitDigitalVoucher(String couponTitle, String voucherCode,
                                     TKPDMapParam<String, String> param,
                                     Subscriber<CouponViewModel> subscriber) {
        compositeSubscription.add(
                tokoplusRepository.checkDigitalCouponValidity(param, voucherCode, couponTitle)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void postCouponValidateRedeem(RequestBodyValidateRedeem requestBodyValidateRedeem,
                                         Subscriber<String> subscriber) {

    }

    @Override
    public void postCouponRedeem(RequestBodyCouponRedeem requestBodyCouponRedeem,
                                 Subscriber<String> subscriber) {

    }

    @Override
    public void getPointRecentHistory(TKPDMapParam<String, String> param, Subscriber<String> subscriber) {

    }

    @Override
    public void getPointMain(TKPDMapParam<String, String> param, Subscriber<String> subscriber) {

    }

    @Override
    public void getPointDrawer(TKPDMapParam<String, String> param, Subscriber<String> subscriber) {

    }

    @Override
    public void getPointStatus(TKPDMapParam<String, String> param, Subscriber<String> subscriber) {

    }

    @Override
    public void getCatalogList(TKPDMapParam<String, String> param, Subscriber<String> subscriber) {

    }

    @Override
    public void getCatalogDetail(TKPDMapParam<String, String> param, Subscriber<String> subscriber) {

    }

    @Override
    public void getCatalogFilterCategory(TKPDMapParam<String, String> param, Subscriber<String> subscriber) {

    }
}
