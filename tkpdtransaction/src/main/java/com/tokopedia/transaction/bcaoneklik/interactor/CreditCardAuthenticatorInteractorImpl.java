package com.tokopedia.transaction.bcaoneklik.interactor;

import com.google.gson.JsonObject;
import com.tokopedia.transaction.bcaoneklik.domain.CreditCardListRepository;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorUpdateWhiteListResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 10/11/17. Tokopedia
 */

public class CreditCardAuthenticatorInteractorImpl implements CreditCardAuthenticatorInteractor{

    private CompositeSubscription compositeSubscription;

    private CreditCardListRepository repository;

    public CreditCardAuthenticatorInteractorImpl(CompositeSubscription compositeSubscription,
                                                 CreditCardListRepository repository) {
        this.compositeSubscription = compositeSubscription;
        this.repository = repository;
    }

    @Override
    public void updateAuthenticationStatus(
            JsonObject requestBody,
            Subscriber<AuthenticatorUpdateWhiteListResponse> subscriber
    ) {
        compositeSubscription.add(repository.updateCreditCardWhiteList(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }
}
