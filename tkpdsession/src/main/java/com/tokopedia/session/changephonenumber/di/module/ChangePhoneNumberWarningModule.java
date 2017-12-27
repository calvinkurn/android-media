package com.tokopedia.session.changephonenumber.di.module;

import android.os.Bundle;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.changephonenumber.data.factory.ChangePhoneNumberFactory;
import com.tokopedia.session.changephonenumber.data.repository.ChangePhoneNumberRepositoryImpl;
import com.tokopedia.session.changephonenumber.di.scope.ChangePhoneNumberWarningScope;
import com.tokopedia.session.changephonenumber.domain.ChangePhoneNumberRepository;
import com.tokopedia.session.changephonenumber.domain.interactor.GetWarningUseCase;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;
import com.tokopedia.session.changephonenumber.view.presenter.ChangePhoneNumberWarningPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by milhamj on 27/12/17.
 */

@ChangePhoneNumberWarningScope
@Module
public class ChangePhoneNumberWarningModule {
    public ChangePhoneNumberWarningModule() {
    }

    @ChangePhoneNumberWarningScope
    @Provides
    ChangePhoneNumberWarningFragmentListener.Presenter provideChangePhoneNumberWarningPresenter(GetWarningUseCase getWarningUseCase) {
        return new ChangePhoneNumberWarningPresenter(getWarningUseCase);
    }

    @ChangePhoneNumberWarningScope
    @Provides
    ChangePhoneNumberRepository provideChangePhoneNumberRepository(ChangePhoneNumberFactory changePhoneNumberFactory) {
        return new ChangePhoneNumberRepositoryImpl(changePhoneNumberFactory);
    }

    @ChangePhoneNumberWarningScope
    @Provides
    AccountsService provideAccountsService() {
        Bundle bundle = new Bundle();
        String authKey = SessionHandler.getAccessToken();
        authKey = "Bearer " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        return new AccountsService(bundle);
    }
}
