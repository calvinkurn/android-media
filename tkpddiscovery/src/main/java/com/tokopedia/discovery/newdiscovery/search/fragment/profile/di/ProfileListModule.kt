package com.tokopedia.profile.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProfileListUseCase
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.ProfileContract
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.ProfileListPresenter
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase

import dagger.Module
import dagger.Provides

@Module
class ProfileListModule {
    @ProfileListScope
    @Provides
    fun provideProfilePresenter(getProfileListUseCase: GetProfileListUseCase,
                                followKolPostGqlUseCase: FollowKolPostGqlUseCase): ProfileContract.Presenter {
        return ProfileListPresenter(getProfileListUseCase,
                followKolPostGqlUseCase)
    }

    @ProfileListScope
    @Provides
    fun provideGetProfileListUseCase(@ApplicationContext context : Context, graphqlUseCase : GraphqlUseCase): GetProfileListUseCase {
        return GetProfileListUseCase(context, graphqlUseCase)
    }

    @ProfileListScope
    @Provides
    fun provideFollowKolUseCase(@ApplicationContext context : Context, graphqlUseCase : GraphqlUseCase): FollowKolPostGqlUseCase {
        return FollowKolPostGqlUseCase(context, graphqlUseCase)
    }

    @ProfileListScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}