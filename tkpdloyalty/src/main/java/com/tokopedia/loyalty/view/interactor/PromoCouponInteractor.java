package com.tokopedia.loyalty.view.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyCouponRedeem;
import com.tokopedia.loyalty.domain.entity.request.RequestBodyValidateRedeem;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.loyalty.view.data.CouponViewModel;
import com.tokopedia.loyalty.view.data.CouponsDataWrapper;
import com.tokopedia.transactiondata.repository.ICartRepository;

import javax.inject.Inject;

import rx.Observable;
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
    private final ICartRepository cartRepository;

    @Inject
    public PromoCouponInteractor(CompositeSubscription compositeSubscription,
                                 ITokoPointRepository tokoplusRepository,
                                 ICartRepository cartRepository) {
        this.compositeSubscription = compositeSubscription;
        this.tokoplusRepository = tokoplusRepository;
        this.cartRepository = cartRepository;
    }


    @Override
    public void getCouponList(TKPDMapParam<String, String> param,
                              Subscriber<CouponsDataWrapper> subscriber) {
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

    @Override
    public void unsubscribe() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void submitVoucherMarketPlaceCartList(Observable observable, Subscriber<?> subscriber) {
        compositeSubscription.add(
                observable.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

}
