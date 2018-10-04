package com.tokopedia.profilecompletion.di;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.util.SessionHandler;
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
public class ProfileCompletionModule {

    @ProfileCompletionScope
    @Provides
    ProfileSourceFactory provideProfileCompletionFactory(
            @ApplicationContext Context context,
            AccountsService accountsService,
            GetUserInfoMapper getUserInfoMapper,
            EditUserInfoMapper editUserInfoMapper,
            SessionHandler sessionHandler) {
        return new ProfileSourceFactory(context, accountsService, getUserInfoMapper,
                editUserInfoMapper, sessionHandler);
    }

    @ProfileCompletionScope
    @Provides
    GetUserInfoMapper provideGetUserInfoMapper() {
        return new GetUserInfoMapper();
    }

    @ProfileCompletionScope
    @Provides
    EditUserInfoMapper provideEdittUserInfoMapper() {
        return new EditUserInfoMapper();
    }

    @ProfileCompletionScope
    @Provides
    ProfileRepository provideProfileRepository(ProfileSourceFactory profileSourceFactory) {
        return new ProfileRepositoryImpl(profileSourceFactory);
    }

    @ProfileCompletionScope
    @Provides
    EditUserProfileUseCase provideEditUserProfileUseCase(ThreadExecutor threadExecutor,
                                                         PostExecutionThread postExecutor,
                                                         ProfileRepository profileRepository) {

        return new EditUserProfileUseCase(threadExecutor, postExecutor, profileRepository);
    }

    @ProfileCompletionScope
    @Provides
    GetUserInfoUseCase provideGetUserProfileUseCase(ProfileRepository profileRepository) {

        return new GetUserInfoUseCase(profileRepository);
    }

    @ProfileCompletionScope
    @Provides
    Bundle provideAccountsBundle(@ApplicationContext Context context,
                                 SessionHandler sessionHandler) {
        Bundle bundle = new Bundle();
        String authKey = sessionHandler.getAccessToken(context);
        authKey = sessionHandler.getTokenType(context) + " " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        return bundle;
    }


    @ProfileCompletionScope
    @Provides
    AccountsService provideAccountsService(SessionHandler sessionHandler,
                                           Bundle bundle) {
        return new AccountsService(bundle);
    }
}
