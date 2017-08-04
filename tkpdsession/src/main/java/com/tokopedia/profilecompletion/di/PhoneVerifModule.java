package com.tokopedia.profilecompletion.di;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.data.factory.OtpSourceFactory;
import com.tokopedia.otp.data.mapper.RequestOtpMapper;
import com.tokopedia.otp.data.mapper.ValidateOtpMapper;
import com.tokopedia.otp.data.repository.OtpRepositoryImpl;
import com.tokopedia.otp.domain.OtpRepository;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.domain.interactor.ValidateOtpUseCase;
import com.tokopedia.otp.phoneverification.data.factory.MsisdnSourceFactory;
import com.tokopedia.otp.phoneverification.data.mapper.ChangePhoneNumberMapper;
import com.tokopedia.otp.phoneverification.data.mapper.VerifyPhoneNumberMapper;
import com.tokopedia.otp.phoneverification.data.repository.MsisdnRepositoryImpl;
import com.tokopedia.otp.phoneverification.domain.MsisdnRepository;
import com.tokopedia.otp.phoneverification.domain.interactor.VerifyPhoneNumberUseCase;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.EditUserInfoMapper;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepository;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;

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
            @ActivityContext Context context){
        return new OtpSourceFactory(context);
    }
    @PhoneVerifScope
    @Provides
    MsisdnSourceFactory provideMsisdnFactory(
            @ActivityContext Context context,
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
    SessionHandler provideSessionHandler(@ActivityContext Context context) {

        return new SessionHandler(context);
    }

    @PhoneVerifScope
    @Provides
    Bundle provideAccountsBundle(@ActivityContext Context context,
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
