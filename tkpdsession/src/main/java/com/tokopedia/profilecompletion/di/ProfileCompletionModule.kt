package com.tokopedia.profilecompletion.di

import android.content.Context
import android.os.Bundle
import com.tokopedia.core.base.di.qualifier.ApplicationContext
import com.tokopedia.core.base.domain.executor.PostExecutionThread
import com.tokopedia.core.base.domain.executor.ThreadExecutor
import com.tokopedia.core.util.SessionHandler
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.service.AccountsService
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory
import com.tokopedia.profilecompletion.data.mapper.EditUserInfoMapper
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper
import com.tokopedia.profilecompletion.data.repository.ProfileRepository
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main

/**
 * Created by stevenfredian on 7/10/17.
 */
@Module
class ProfileCompletionModule {
    @ProfileCompletionScope
    @Provides
    fun provideProfileCompletionFactory(
            @ApplicationContext context: Context,
            accountsService: AccountsService,
            getUserInfoMapper: GetUserInfoMapper,
            editUserInfoMapper: EditUserInfoMapper,
            sessionHandler: SessionHandler): ProfileSourceFactory {
        return ProfileSourceFactory(context, accountsService, getUserInfoMapper,
                editUserInfoMapper, sessionHandler)
    }

    @ProfileCompletionScope
    @Provides
    fun provideGetUserInfoMapper(): GetUserInfoMapper {
        return GetUserInfoMapper()
    }

    @ProfileCompletionScope
    @Provides
    fun provideEdittUserInfoMapper(): EditUserInfoMapper {
        return EditUserInfoMapper()
    }

    @ProfileCompletionScope
    @Provides
    fun provideProfileRepository(profileSourceFactory: ProfileSourceFactory): ProfileRepository {
        return ProfileRepositoryImpl(profileSourceFactory)
    }

    @ProfileCompletionScope
    @Provides
    fun provideEditUserProfileUseCase(threadExecutor: ThreadExecutor,
                                      postExecutor: PostExecutionThread,
                                      profileRepository: ProfileRepository): EditUserProfileUseCase {
        return EditUserProfileUseCase(threadExecutor, postExecutor, profileRepository)
    }

    @ProfileCompletionScope
    @Provides
    fun provideGetUserProfileUseCase(profileRepository: ProfileRepository): GetUserInfoUseCase {
        return GetUserInfoUseCase(profileRepository)
    }

    @ProfileCompletionScope
    @Provides
    fun provideAccountsBundle(@ApplicationContext context: Context?,
                              sessionHandler: SessionHandler): Bundle {
        val bundle = Bundle()
        val userSession: UserSessionInterface = UserSession(context)
        var authKey = userSession.accessToken
        authKey = sessionHandler.getTokenType(context) + " " + authKey
        bundle.putString(AccountsService.AUTH_KEY, authKey)
        return bundle
    }

    @ProfileCompletionScope
    @Provides
    fun provideAccountsService(sessionHandler: SessionHandler?,
                               bundle: Bundle?): AccountsService {
        return AccountsService(bundle)
    }

    @ProfileCompletionScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSession {
        return UserSession(context)
    }

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository {
        return getInstance().graphqlRepository
    }

    @ProfileCompletionScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }
}