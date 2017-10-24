package com.tokopedia.profilecompletion.di;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.data.factory.OtpSourceFactory;
import com.tokopedia.otp.domain.mapper.OldRequestOtpMapper;
import com.tokopedia.otp.domain.mapper.OldValidateOtpMapper;
import com.tokopedia.otp.data.repository.OtpRepositoryImpl;
import com.tokopedia.otp.domain.OtpRepository;
import com.tokopedia.otp.domain.interactor.OldRequestOtpUseCase;
import com.tokopedia.otp.domain.interactor.OldValidateOtpUseCase;
import com.tokopedia.otp.phoneverification.data.factory.MsisdnSourceFactory;
import com.tokopedia.otp.phoneverification.domain.mapper.ChangePhoneNumberMapper;
import com.tokopedia.otp.phoneverification.domain.mapper.VerifyPhoneNumberMapper;
import com.tokopedia.otp.phoneverification.data.repository.MsisdnRepositoryImpl;
import com.tokopedia.otp.phoneverification.domain.MsisdnRepository;
import com.tokopedia.otp.phoneverification.domain.interactor.VerifyPhoneNumberUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by stevenfredian on 7/10/17.
 */

@Module
public class PhoneVerifModule {

    @PhoneVerifScope
    @Provides
    OtpSourceFactory provideOtpFactory(
            @ApplicationContext Context context) {
        return new OtpSourceFactory(context);
    }
    @PhoneVerifScope
    @Provides
    MsisdnSourceFactory provideMsisdnFactory(
            @ApplicationContext Context context,
            AccountsService accountsService,
            ChangePhoneNumberMapper changePhoneNumberMapper,
            VerifyPhoneNumberMapper verifyPhoneNumberMapper){
        return new MsisdnSourceFactory(context, accountsService, verifyPhoneNumberMapper, changePhoneNumberMapper);
    }

    @PhoneVerifScope
    @Provides
    ChangePhoneNumberMapper provideChangePhoneNumberMapper(){
        return new ChangePhoneNumberMapper();
    }

    @PhoneVerifScope
    @Provides
    VerifyPhoneNumberMapper provideVerifyPhoneNumberMapper(){
        return new VerifyPhoneNumberMapper();
    }

    @PhoneVerifScope
    @Provides
    OldRequestOtpMapper provideRequestOtpMapper(){
        return new OldRequestOtpMapper();
    }

    @PhoneVerifScope
    @Provides
    OldValidateOtpMapper provideValidateOtpMapper(){
        return new OldValidateOtpMapper();
    }

    @PhoneVerifScope
    @Provides
    OtpRepository provideOtpRepository(OtpSourceFactory otpSourceFactory) {
        return new OtpRepositoryImpl(otpSourceFactory);
    }

    @PhoneVerifScope
    @Provides
    MsisdnRepository provideMsisdnRepository(MsisdnSourceFactory msisdnSourceFactory) {
        return new MsisdnRepositoryImpl(msisdnSourceFactory);
    }

    @PhoneVerifScope
    @Provides
    OldRequestOtpUseCase provideRequestOtpUseCase(ThreadExecutor threadExecutor,
                                                  PostExecutionThread postExecutor,
                                                  OtpRepository otpRepository){

        return new OldRequestOtpUseCase(threadExecutor, postExecutor, otpRepository);
    }

    @PhoneVerifScope
    @Provides
    OldValidateOtpUseCase provideValidateOtpUseCase(ThreadExecutor threadExecutor,
                                                    PostExecutionThread postExecutor,
                                                    OtpRepository otpRepository){

        return new OldValidateOtpUseCase(threadExecutor, postExecutor, otpRepository);
    }

    @PhoneVerifScope
    @Provides
    Bundle provideAccountsBundle(@ApplicationContext Context context,
                                 SessionHandler sessionHandler) {
        Bundle bundle = new Bundle();
        String authKey = sessionHandler.getAccessToken(context);
        authKey = sessionHandler.getTokenType(context) + " " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        bundle.putBoolean(AccountsService.USING_BOTH_AUTHORIZATION, true);
        return bundle;
    }


    @PhoneVerifScope
    @Provides
    AccountsService provideAccountsService(SessionHandler sessionHandler,
                                           Bundle bundle) {
        return new AccountsService(bundle);
    }
}
