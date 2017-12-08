package com.tokopedia.loyalty.view.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.loyalty.view.data.VoucherViewModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCodeInteractor implements IPromoCodeInteractor {

    private final CompositeSubscription compositeSubscription;
    private final ITokoPointRepository tokoplusRepositoy;

    @Inject
    public PromoCodeInteractor(CompositeSubscription compositeSubscription,
                               ITokoPointRepository repository) {
        this.compositeSubscription = compositeSubscription;
        this.tokoplusRepositoy = repository;
    }

    @Override
    public void submitVoucher(String voucherCode,
                              TKPDMapParam<String, String> params,
                              Subscriber<VoucherViewModel> subscriber) {
        compositeSubscription.add(
                tokoplusRepositoy.checkVoucherValidity(params, voucherCode)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void submitDigitalVoucher(String voucherCode,
                                     TKPDMapParam<String, String> param, Subscriber<VoucherViewModel> subscriber) {
        compositeSubscription.add(
                tokoplusRepositoy.checkDigitalVoucherValidity(param, voucherCode)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }
}
