package com.tokopedia.di;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
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
import com.tokopedia.session.domain.mapper.DiscoverMapper;
import com.tokopedia.session.domain.mapper.MakeLoginMapper;
import com.tokopedia.session.domain.mapper.TokenMapper;
import com.tokopedia.session.register.data.mapper.CreatePasswordMapper;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;


/**
 * @author by nisie on 10/10/17.
 */

@Module
public class
SessionModule {

    private static final String HMAC_SERVICE = "HMAC_SERVICE";
    private static final String BASIC_SERVICE = "BASIC_SERVICE";
    private static final String BEARER_SERVICE = "BEARER_SERVICE";
    private static final String WS_SERVICE = "WS_SERVICE";

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
     * @return https://accounts.tokopedia.com
     * with Authorization : Basic
     */
    @SessionScope
    @Named(BASIC_SERVICE)
    @Provides
    AccountsService provideBasicAccountsService() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.IS_BASIC, true);
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
    GetTokenDataSource provideGetTokenDataSource(@Named(BASIC_SERVICE) AccountsService
                                                         accountsService,
                                                 TokenMapper tokenMapper,
                                                 SessionHandler sessionHandler) {
        return new GetTokenDataSource(accountsService, tokenMapper, sessionHandler);
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
                                                      ChangePhoneNumberMapper changePhoneNumberMapper,
                                                      SessionHandler sessionHandler) {
        return new ChangeMsisdnSource(accountsService, changePhoneNumberMapper, sessionHandler);
    }

    @SessionScope
    @Provides
    VerifyMsisdnSource provideVerifyMsisdnSource(@Named(BEARER_SERVICE) AccountsService accountsService,
                                                 VerifyPhoneNumberMapper verifyPhoneNumberMapper,
                                                 SessionHandler sessionHandler) {
        return new VerifyMsisdnSource(accountsService, verifyPhoneNumberMapper, sessionHandler);
    }

    @SessionScope
    @Provides
    CreatePasswordDataSource provideCreatePasswordDataSource(@Named(BEARER_SERVICE) AccountsService
                                                                     accountsService,
                                                             CreatePasswordMapper createPasswordMapper) {
        return new CreatePasswordDataSource(accountsService, createPasswordMapper);
    }
}
