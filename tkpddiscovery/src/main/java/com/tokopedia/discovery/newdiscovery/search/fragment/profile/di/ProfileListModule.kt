package com.tokopedia.profile.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProfileListUseCase
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.ProfileContract
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.ProfileListPresenter
import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides

@Module
class ProfileListModule {
    @ProfileListScope
    @Provides
    fun provideProfilePresenter(getProfileListUseCase: GetProfileListUseCase): ProfileContract.Presenter {
        return ProfileListPresenter(getProfileListUseCase)
    }

    @ProfileListScope
    @Provides
    fun provideGetProfileListUseCase(@ApplicationContext context : Context, graphqlUseCase : GraphqlUseCase): GetProfileListUseCase {
        return GetProfileListUseCase(context, graphqlUseCase)
    }
}