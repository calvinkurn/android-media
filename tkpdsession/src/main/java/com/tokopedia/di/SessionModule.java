package com.tokopedia.di;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.service.AccountsBasicService;
import com.tokopedia.otp.data.source.OtpSource;
import com.tokopedia.otp.domain.mapper.RequestOtpMapper;
import com.tokopedia.otp.domain.mapper.ValidateOtpMapper;
import com.tokopedia.otp.phoneverification.data.source.ChangeMsisdnSource;

import com.tokopedia.otp.phoneverification.data.source.VerifyMsisdnSource;
import com.tokopedia.otp.phoneverification.domain.mapper.ChangePhoneNumberMapper;
import com.tokopedia.otp.phoneverification.domain.mapper.VerifyPhoneNumberMapper;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.EditUserInfoMapper;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepository;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.session.data.source.CloudDiscoverDataSource;
import com.tokopedia.session.data.source.CreatePasswordDataSource;
import com.tokopedia.session.data.source.GetTokenDataSource;
import com.tokopedia.session.data.source.MakeLoginDataSource;
import com.tokopedia.session.domain.interactor.DiscoverUseCase;
import com.tokopedia.session.domain.mapper.DiscoverMapper;
import com.tokopedia.session.domain.mapper.MakeLoginMapper;
import com.tokopedia.session.domain.mapper.TokenMapper;
import com.tokopedia.session.login.loginemail.domain.interactor.LoginEmailUseCase;
import com.tokopedia.session.login.loginemail.view.presenter.LoginPresenter;
import com.tokopedia.session.register.data.mapper.CreatePasswordMapper;
import com.tokopedia.session.changephonenumber.data.repository.ChangePhoneNumberRepositoryImpl;
import com.tokopedia.session.changephonenumber.data.source.CloudGetWarningSource;
import com.tokopedia.session.changephonenumber.data.source.CloudSendEmailSource;
import com.tokopedia.session.changephonenumber.data.source.CloudValidateEmailCodeSource;
import com.tokopedia.session.changephonenumber.data.source.CloudValidateNumberSource;
import com.tokopedia.session.changephonenumber.domain.ChangePhoneNumberRepository;
import com.tokopedia.session.changephonenumber.domain.interactor.GetWarningUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.SendEmailUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.ValidateEmailCodeUseCase;
import com.tokopedia.session.changephonenumber.domain.interactor.ValidateNumberUseCase;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailVerificationFragmentListener;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener;
import com.tokopedia.session.changephonenumber.view.presenter.ChangePhoneNumberEmailVerificationPresenter;
import com.tokopedia.session.changephonenumber.view.presenter.ChangePhoneNumberInputPresenter;
import com.tokopedia.session.changephonenumber.view.presenter.ChangePhoneNumberWarningPresenter;
import com.tokopedia.session.data.source.GetTokenDataSource;
import com.tokopedia.session.data.source.MakeLoginDataSource;
import com.tokopedia.session.domain.mapper.MakeLoginMapper;
import com.tokopedia.session.domain.mapper.TokenMapper;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;


/**
 * @author by nisie on 10/10/17.
 */

@Module
public class
SessionModule {

    public static final String HMAC_SERVICE = "HMAC_SERVICE";
    public static final String BEARER_SERVICE = "BEARER_SERVICE";
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
    CloudDiscoverDataSource provideCloudDiscoverDataSource(GlobalCacheManager globalCacheManager,
                                                           @Named(HMAC_SERVICE) AccountsService
                                                                   accountsService,
                                                           DiscoverMapper discoverMapper) {
        return new CloudDiscoverDataSource(globalCacheManager, accountsService, discoverMapper);
    }

    @SessionScope
    @Provides
    GetTokenDataSource provideGetTokenDataSource(AccountsBasicService
                                                         accountsService,
                                                 TokenMapper tokenMapper,
                                                 SessionHandler sessionHandler) {
        return new GetTokenDataSource(accountsService, tokenMapper, sessionHandler);
    }

    @SessionScope
    @Provides
    GetUserInfoMapper provideGetUserInfoMapper() {
        return new GetUserInfoMapper();
    }

    @SessionScope
    @Provides
    EditUserInfoMapper provideEditUserInfoMapper() {
        return new EditUserInfoMapper();
    }

    @SessionScope
    @Provides
    ChangePhoneNumberInputFragmentListener.Presenter provideChangePhoneNumberInputPresenter(ValidateNumberUseCase validateNumberUseCase) {
        return new ChangePhoneNumberInputPresenter(validateNumberUseCase);
    }

    @SessionScope
    @Provides
    ChangePhoneNumberWarningFragmentListener.Presenter provideChangePhoneNumberWarningPresenter(GetWarningUseCase getWarningUseCase) {
        return new ChangePhoneNumberWarningPresenter(getWarningUseCase);
    }

    @SessionScope
    @Provides
    ChangePhoneNumberRepository provideChangePhoneNumberRepository(CloudGetWarningSource cloudGetWarningSource,
                                                                   CloudSendEmailSource cloudSendEmailSource,
                                                                   CloudValidateNumberSource cloudValidateNumberSource,
                                                                   CloudValidateEmailCodeSource cloudValidateEmailCodeSource) {
        return new ChangePhoneNumberRepositoryImpl(cloudGetWarningSource,
                cloudSendEmailSource,
                cloudValidateNumberSource,
                cloudValidateEmailCodeSource);
    }

    @SessionScope
    @Provides
    ChangePhoneNumberEmailVerificationFragmentListener.Presenter ChangePhoneNumberEmailVerificationPresenter(SendEmailUseCase sendEmailUseCase,
                                                                                                             ValidateEmailCodeUseCase validateEmailCodeUseCase) {
        return new ChangePhoneNumberEmailVerificationPresenter(sendEmailUseCase, validateEmailCodeUseCase);
    }


    @SessionScope
    @Provides
    ProfileSourceFactory provideProfileSourceFactory(@ApplicationContext Context context,
                                                     @Named(BEARER_SERVICE) AccountsService accountsService,
                                                     GetUserInfoMapper getUserInfoMapper,
                                                     EditUserInfoMapper editUserInfoMapper,
                                                     SessionHandler sessionHandler) {
        return new ProfileSourceFactory(
                context, accountsService,
                getUserInfoMapper, editUserInfoMapper,
                sessionHandler);
    }

    @SessionScope
    @Provides
    ProfileRepository provideProfileRepository(ProfileSourceFactory profileSourceFactory) {
        return new ProfileRepositoryImpl(profileSourceFactory);
    }

    @SessionScope
    @Provides
    GetUserInfoUseCase provideGetUserInfoUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread,
                                                 ProfileRepository profileRepository) {
        return new GetUserInfoUseCase(threadExecutor, postExecutionThread, profileRepository);
    }

    @SessionScope
    @Provides
    MakeLoginDataSource provideMakeLoginDataSource(@Named(WS_SERVICE) AccountsService accountsService,
                                                   MakeLoginMapper makeLoginMapper,
                                                   SessionHandler sessionHandler) {
        return new MakeLoginDataSource(accountsService, makeLoginMapper, sessionHandler);
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
    ChangeMsisdnSource provideCloudChangeMsisdnSource(@Named(BEARER_SERVICE) AccountsService accountsService,
                                                      ChangePhoneNumberMapper changePhoneNumberMapper) {
        return new ChangeMsisdnSource(accountsService, changePhoneNumberMapper);
    }

    @SessionScope
    @Provides
    VerifyMsisdnSource provideVerifyMsisdnSource(@Named(BEARER_SERVICE) AccountsService accountsService,
                                                 VerifyPhoneNumberMapper verifyPhoneNumberMapper) {
        return new VerifyMsisdnSource(accountsService, verifyPhoneNumberMapper);
    }

    @SessionScope
    @Provides
    CreatePasswordDataSource provideCreatePasswordDataSource(@Named(BEARER_SERVICE) AccountsService
                                                                     accountsService,
                                                             CreatePasswordMapper createPasswordMapper) {
        return new CreatePasswordDataSource(accountsService, createPasswordMapper);
    }

    @SessionScope
    @Provides
    @Named(LOGIN_CACHE)
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, LOGIN_CACHE);
    }

}
