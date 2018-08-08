package com.tokopedia.transaction.bcaoneklik.di;

import com.tokopedia.core.network.apiservices.payment.BcaOneClickService;
import com.tokopedia.core.network.apiservices.transaction.CreditCardAuthService;
import com.tokopedia.core.network.apiservices.transaction.CreditCardVaultService;
import com.tokopedia.core.network.apiservices.transaction.apis.CreditCardVaultApi;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.transaction.bcaoneklik.domain.BcaOneClickFormRepository;
import com.tokopedia.transaction.bcaoneklik.domain.CreditCardListRepository;
import com.tokopedia.transaction.bcaoneklik.domain.ListPaymentRepository;
import com.tokopedia.transaction.bcaoneklik.domain.creditcardauthentication.UserInfoRepository;
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
    PeopleService providePeopleService() {
        return new PeopleService();
    }

    @Provides
    @PaymentOptionScope
    UserInfoRepository providePeopleInfoRepository() {
        return new UserInfoRepository(providePeopleService());
    }

    @Provides
    @PaymentOptionScope
    BcaOneClickFormRepository provideBcaOneClickRepository() {
        return new BcaOneClickFormRepository(provideBcaOneClickService());
    }

    @Provides
    @PaymentOptionScope
    CreditCardAuthService provideCreditCardAuthService() {
        return new CreditCardAuthService();
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
    BcaOneClickService provideBcaOneClickService() {
        return new BcaOneClickService();
    }

    @Provides
    @PaymentOptionScope
    CreditCardListRepository provideCreditCardListRepository() {
        return new CreditCardListRepository(provideCreditCardService(),
                provideCreditCardAuthService());
    }

    @Provides
    @PaymentOptionScope
    ListPaymentRepository provideListPaymentRepository() {
        return new ListPaymentRepository();
    }

    @Provides
    @PaymentOptionScope
    PaymentListInteractorImpl provideInteractorImpl() {
        return new PaymentListInteractorImpl(
                provideCompositeSubscription(),
                provideCreditCardListRepository(),
                provideBcaOneClickRepository(),
                provideListPaymentRepository(),
                providePeopleInfoRepository());
    }

    @Provides
    @PaymentOptionScope
    ListPaymentTypePresenterImpl providePresenter() {
        return new ListPaymentTypePresenterImpl(provideInteractorImpl());
    }

 }
