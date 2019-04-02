package com.tokopedia.tkpdpdp.estimasiongkir.di

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tkpdpdp.estimasiongkir.domain.interactor.GetRateEstimationUseCase

import dagger.Module
import dagger.Provides

@Module
@RatesEstimationScope
class RatesEstimationModule {

    @RatesEstimationScope
    @Provides
    fun provideGraphqlUseCase() = GraphqlUseCase()

    @RatesEstimationScope
    @Provides
    fun provideGetRateEstimationUseCase(graphqlUseCase: GraphqlUseCase): GetRateEstimationUseCase {
        return GetRateEstimationUseCase(graphqlUseCase)
    }
}
