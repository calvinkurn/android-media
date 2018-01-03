package com.tokopedia.di;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.data.source.OtpSource;
import com.tokopedia.otp.domain.mapper.RequestOtpMapper;
import com.tokopedia.otp.domain.mapper.ValidateOtpMapper;
import com.tokopedia.profilecompletion.data.mapper.EditUserInfoMapper;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepository;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailVerificationFragmentListener;
import com.tokopedia.session.changephonenumber.view.presenter.ChangePhoneNumberEmailVerificationPresenter;
import com.tokopedia.session.login.domain.mapper.MakeLoginMapper;
import com.tokopedia.session.changephonenumber.data.repository.ChangePhoneNumberRepositoryImpl;
import com.tokopedia.session.changephonenumber.data.source.CloudGetWarningSource;
import com.tokopedia.session.changephonenumber.data.source.CloudSendEmailSource;
import com.tokopedia.session.changephonenumber.domain.ChangePhoneNumberRepository;
import com.tokopedia.session.changephonenumber.domain.interactor.GetWarningUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.SendEmailUseCase;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailFragmentListener;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;
import com.tokopedia.session.changephonenumber.view.presenter.ChangePhoneNumberEmailPresenter;
import com.tokopedia.session.changephonenumber.view.presenter.ChangePhoneNumberInputPresenter;
import com.tokopedia.session.changephonenumber.view.presenter.ChangePhoneNumberWarningPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;


/**
 * @author by nisie on 10/10/17.
 */

@Module
public class
SessionModule {

    public static final String BEARER_SERVICE = "BEARER_SERVICE";
    private static final String HMAC_SERVICE = "HMAC_SERVICE";
    private static final String WS_SERVICE = "WS_SERVICE";
    public static final String LOGIN_CACHE = "LOGIN_CACHE";

    @SessionScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    /**
     * @return https://accounts.tokopedia.com
     * with Authorization : Tkpd
     */
    @SessionScope
    @Named(HMAC_SERVICE)
    @Provides
    AccountsService provideHMACAccountsService() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);
        return new AccountsService(bundle);
    }

    /**
     * @param context
     * @param sessionHandler
     * @return https://accounts.tokopedia.com
     * with Authorization : Bearer {Access Token}
     */
    @SessionScope
    @Named(BEARER_SERVICE)
    @Provides
    AccountsService provideBearerAccountsService(@ApplicationContext Context context,
                                                 SessionHandler sessionHandler) {
        Bundle bundle = new Bundle();
        String authKey = "";
        if (!TextUtils.isEmpty(sessionHandler.getAccessToken(context)))
            authKey = sessionHandler.getTokenType(context) + " " + sessionHandler
                    .getAccessToken(context);
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        return new AccountsService(bundle);
    }


    /**
     * @param context
     * @param sessionHandler
     * @return https://ws.tokopedia.com
     * with Authorization : Bearer {Access Token}
     */
    @SessionScope
    @Named(WS_SERVICE)
    @Provides
    AccountsService provideWsAccountsService(@ApplicationContext Context context,
                                             SessionHandler sessionHandler) {
        Bundle bundle = new Bundle();
        String authKey;
        authKey = sessionHandler.getTokenType(context) + " " + sessionHandler.getAccessToken(context);
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        bundle.putString(AccountsService.WEB_SERVICE, AccountsService.WS);
        return new AccountsService(bundle);
    }

    @SessionScope
    @Provides
    ChangePhoneNumberInputFragmentListener.Presenter provideChangePhoneNumberInputPresenter() {
        return new ChangePhoneNumberInputPresenter();
    }


    @SessionScope
    @Provides
    ChangePhoneNumberWarningFragmentListener.Presenter provideChangePhoneNumberWarningPresenter(GetWarningUseCase getWarningUseCase) {
        return new ChangePhoneNumberWarningPresenter(getWarningUseCase);
    }

    @SessionScope
    @Provides
    ChangePhoneNumberRepository provideChangePhoneNumberRepository(CloudGetWarningSource cloudGetWarningSource,
                                                                   CloudSendEmailSource cloudSendEmailSource) {
        return new ChangePhoneNumberRepositoryImpl(cloudGetWarningSource, cloudSendEmailSource);
    }

    @SessionScope
    @Provides
    ChangePhoneNumberEmailFragmentListener.Presenter ChangePhoneNumberEmailPresenter(SendEmailUseCase sendEmailUseCase) {
        return new ChangePhoneNumberEmailPresenter(sendEmailUseCase);
    }

    @SessionScope
    @Provides
    ChangePhoneNumberEmailVerificationFragmentListener.Presenter ChangePhoneNumberEmailVerificationPresenter() {
        return new ChangePhoneNumberEmailVerificationPresenter();
    }

    @SessionScope
    @Provides
    OtpSource provideOtpSource(@Named(BEARER_SERVICE) AccountsService accountsService,
                               RequestOtpMapper requestOTPMapper,
                               ValidateOtpMapper validateOTPMapper,
                               SessionHandler sessionHandler) {
        return new OtpSource(accountsService, requestOTPMapper, validateOTPMapper, sessionHandler);
    }

    @SessionScope
    @Provides
    @Named(LOGIN_CACHE)
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, LOGIN_CACHE);
    }

}
