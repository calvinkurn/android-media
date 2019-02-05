package com.tokopedia.transaction.others;

import com.tokopedia.core.network.apiservices.transaction.CreditCardAuthService;
import com.tokopedia.core.network.apiservices.transaction.CreditCardVaultService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.transaction.others.creditcardauthentication.UserInfoRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class SingleAuthenticationModule {

    public SingleAuthenticationModule() {

    }

    @Provides
    @SingleAuthenticationScope
    PeopleService providePeopleService() {
        return new PeopleService();
    }

    @Provides
    @SingleAuthenticationScope
    CreditCardVaultService provideVaultService() {
        return new CreditCardVaultService();
    }

    @Provides
    @SingleAuthenticationScope
    CreditCardAuthService authService() {
        return new CreditCardAuthService();
    }

    @Provides
    @SingleAuthenticationScope
    CreditCardListRepository provideCreditCardListRepository(CreditCardVaultService vaultService,
                                                             CreditCardAuthService authService) {
        return new CreditCardListRepository(vaultService, authService);
    }

    @Provides
    @SingleAuthenticationScope
    UserInfoRepository userInfoRepository(PeopleService peopleService) {
        return new UserInfoRepository(peopleService);
    }
}
