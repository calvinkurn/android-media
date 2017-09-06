package com.tokopedia.transaction.bcaoneklik.interactor;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.apiservices.transaction.CreditCardVaultService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.bcaoneklik.domain.CreditCardListRepository;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardSuccessDeleteModel;
import com.tokopedia.transaction.bcaoneklik.presenter.ListPaymentTypePresenter;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 8/21/17. Tokopedia
 */

public class PaymentListInteractorImpl implements IPaymentListInteractor{

    private CompositeSubscription compositeSubscription;
    private CreditCardListRepository creditCardListRepository;
    private CreditCardVaultService service;

    public PaymentListInteractorImpl(CompositeSubscription subscription,
                                     CreditCardListRepository creditCardListRepository,
                                     CreditCardVaultService service) {

        this.compositeSubscription = subscription;
        this.creditCardListRepository = creditCardListRepository;
        this.service = service;
    }

    @Override
    public void getPaymentList(final Subscriber<CreditCardModel> subscriber,
                               JsonObject requestBody) {
        compositeSubscription.add(creditCardListRepository.getCreditCardList(service, requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber)
        );
    }

    @Override
    public void deleteCreditCard(Subscriber<CreditCardSuccessDeleteModel> subscriber,
                                 JsonObject requestBody) {
        compositeSubscription.add(creditCardListRepository.deleteCreditCard(service, requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber)
        );
    }
}
