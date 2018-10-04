package com.tokopedia.transaction.bcaoneklik.di.fingerprint;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.network.apiservices.transaction.CreditCardAuthService;
import com.tokopedia.core.network.apiservices.transaction.CreditCardVaultService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.transaction.bcaoneklik.domain.CreditCardListRepository;
import com.tokopedia.transaction.bcaoneklik.domain.creditcardauthentication.UserInfoRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kris on 4/24/18. Tokopedia
 */

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
