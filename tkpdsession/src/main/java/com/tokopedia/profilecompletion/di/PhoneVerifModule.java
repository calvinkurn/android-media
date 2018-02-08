package com.tokopedia.profilecompletion.di;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.data.factory.OtpSourceFactory;
import com.tokopedia.otp.data.mapper.RequestOtpMapper;
import com.tokopedia.otp.data.mapper.ValidateOtpMapper;
import com.tokopedia.otp.data.repository.OtpRepositoryImpl;
import com.tokopedia.otp.domainold.OtpRepository;
import com.tokopedia.otp.domainold.RequestOtpUseCase;
import com.tokopedia.otp.domainold.ValidateOtpUseCase;
import com.tokopedia.otp.phoneverification.data.factory.MsisdnSourceFactory;
import com.tokopedia.otp.phoneverification.data.mapper.ChangePhoneNumberMapper;
import com.tokopedia.otp.phoneverification.data.mapper.VerifyPhoneNumberMapper;
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
    RequestOtpMapper provideRequestOtpMapper(){
        return new RequestOtpMapper();
    }

    @PhoneVerifScope
    @Provides
    ValidateOtpMapper provideValidateOtpMapper(){
        return new ValidateOtpMapper();
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
    RequestOtpUseCase provideRequestOtpUseCase(ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutor,
                                               OtpRepository otpRepository){

        return new RequestOtpUseCase(threadExecutor, postExecutor, otpRepository);
    }

    @PhoneVerifScope
    @Provides
    ValidateOtpUseCase provideValidateOtpUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutor,
                                                 OtpRepository otpRepository){

        return new ValidateOtpUseCase(threadExecutor, postExecutor, otpRepository);
    }
    @PhoneVerifScope
    @Provides
    VerifyPhoneNumberUseCase provideVerifyPhoneNumberUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutor,
                                                             MsisdnRepository msisdnRepository,
                                                             ValidateOtpUseCase validateOtpUseCase){

        return new VerifyPhoneNumberUseCase(threadExecutor, postExecutor, msisdnRepository,validateOtpUseCase);
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
