package com.tokopedia.transaction.bcaoneklik.di.creditcardauth;

import com.tokopedia.core.network.apiservices.transaction.CreditCardAuthService;
import com.tokopedia.core.network.apiservices.transaction.CreditCardVaultService;
import com.tokopedia.transaction.bcaoneklik.domain.CreditCardListRepository;
import com.tokopedia.transaction.bcaoneklik.interactor.CreditCardAuthenticatorInteractorImpl;
import com.tokopedia.transaction.bcaoneklik.presenter.CreditCardAuthenticationPresenterImpl;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 10/11/17. Tokopedia
 */
@Module
public class CreditCardAuthenticationModule {

    public CreditCardAuthenticationModule() {
    }

    @Provides
    @CreditCardAuthenticationScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @CreditCardAuthenticationScope
    CreditCardVaultService provideCreditCardService() {
        return new CreditCardVaultService();
    }

    @Provides
    @CreditCardAuthenticationScope
    CreditCardAuthService provideCreditCardAuthService() {
        return new CreditCardAuthService();
    }

    @Provides
    @CreditCardAuthenticationScope
    CreditCardListRepository provideCreditCardRepository() {
        return new CreditCardListRepository(
                provideCreditCardService(), provideCreditCardAuthService()
        );
    }

    @Provides
    @CreditCardAuthenticationScope
    CreditCardAuthenticatorInteractorImpl provideInteractor() {
        return new CreditCardAuthenticatorInteractorImpl(
                provideCompositeSubscription(), provideCreditCardRepository());
    }

    @Provides
    @CreditCardAuthenticationScope
    CreditCardAuthenticationPresenterImpl providePresenter() {
        return new CreditCardAuthenticationPresenterImpl(provideInteractor());
    }


}
