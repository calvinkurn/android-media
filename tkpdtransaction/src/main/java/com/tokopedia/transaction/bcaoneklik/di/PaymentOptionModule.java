package com.tokopedia.transaction.bcaoneklik.di;

import com.tokopedia.core.network.apiservices.payment.BcaOneClickService;
import com.tokopedia.core.network.apiservices.transaction.CreditCardVaultService;
import com.tokopedia.core.network.apiservices.transaction.apis.CreditCardVaultApi;
import com.tokopedia.transaction.bcaoneklik.domain.BcaOneClickFormRepository;
import com.tokopedia.transaction.bcaoneklik.domain.CreditCardListRepository;
import com.tokopedia.transaction.bcaoneklik.interactor.PaymentListInteractorImpl;
import com.tokopedia.transaction.bcaoneklik.presenter.ListPaymentTypePresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 8/21/17. Tokopedia
 */
@Module
public class PaymentOptionModule {

    public PaymentOptionModule() {
    }

    @Provides
    @PaymentOptionScope
    CreditCardVaultApi provideCreditCardApi(@PaymentOptionQualifier Retrofit retrofit) {
        return retrofit.create(CreditCardVaultApi.class);
    }

    @Provides
    @PaymentOptionScope
    CreditCardVaultService provideCreditCardService() {
        return new CreditCardVaultService();
    }

    @Provides
    @PaymentOptionScope
    @PaymentOptionQualifier
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @PaymentOptionScope
    ListPaymentTypePresenterImpl providePresenter(
            @PaymentOptionQualifier CompositeSubscription compositeSubscription
    ) {
        return new ListPaymentTypePresenterImpl(compositeSubscription,
                new BcaOneClickFormRepository(new BcaOneClickService()),
                new PaymentListInteractorImpl(compositeSubscription,
                        new CreditCardListRepository(), new CreditCardVaultService()));
    }

 }
