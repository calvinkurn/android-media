package com.tokopedia.loyalty.view.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.transactiondata.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class PromoCodeInteractor implements IPromoCodeInteractor {

    private final CompositeSubscription compositeSubscription;
    private final ITokoPointRepository tokoplusRepositoy;
    private final ICartRepository cartRepository;

    @Inject
    public PromoCodeInteractor(CompositeSubscription compositeSubscription,
                               ITokoPointRepository repository, ICartRepository cartRepository) {
        this.compositeSubscription = compositeSubscription;
        this.tokoplusRepositoy = repository;
        this.cartRepository = cartRepository;
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

    @Override
    public void submitCheckPromoCodeMarketPlace(TKPDMapParam<String, String> paramUpdateCart,
                                                TKPDMapParam<String, String> paramCheckPromo,
                                                Subscriber<VoucherViewModel> subscriber) {


        if (paramUpdateCart != null)
            compositeSubscription.add(cartRepository.updateCartData(paramUpdateCart)
                    .flatMap(updateCartDataResponse -> cartRepository.checkPromoCodeCartList(paramCheckPromo)
                            .map(
                                    getCheckPromoCodeCartListDataResponseVoucherViewModelFunc1()
                            ))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .subscribe(subscriber));
        else
            compositeSubscription.add(cartRepository.checkPromoCodeCartList(paramCheckPromo)
                    .map(
                            getCheckPromoCodeCartListDataResponseVoucherViewModelFunc1()
                    )
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .subscribe(subscriber));


    }

    @NonNull
    private Func1<CheckPromoCodeCartListDataResponse, VoucherViewModel>
    getCheckPromoCodeCartListDataResponseVoucherViewModelFunc1() {
        return checkPromoCodeCartListDataResponse -> {
            VoucherViewModel voucherViewModel = new VoucherViewModel();
            if (checkPromoCodeCartListDataResponse.getError() != null
                    && !checkPromoCodeCartListDataResponse.getError().isEmpty()) {
                voucherViewModel.setSuccess(false);
                voucherViewModel.setMessage(checkPromoCodeCartListDataResponse.getError());
            } else {
                voucherViewModel.setSuccess(true);
                voucherViewModel.setMessage(
                        checkPromoCodeCartListDataResponse.getDataVoucher().getMessageSuccess()
                );
                voucherViewModel.setCode(
                        checkPromoCodeCartListDataResponse.getDataVoucher().getCode()
                );
                voucherViewModel.setAmount(
                        checkPromoCodeCartListDataResponse.getDataVoucher().getCashbackVoucherDescription()
                );
                voucherViewModel.setRawCashback(
                        checkPromoCodeCartListDataResponse.getDataVoucher().getCashbackVoucherAmount()
                );
            }
            return voucherViewModel;
        };
    }
}
