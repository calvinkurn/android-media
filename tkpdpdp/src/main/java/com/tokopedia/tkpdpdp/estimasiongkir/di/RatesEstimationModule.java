package com.tokopedia.tkpdpdp.estimasiongkir.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tkpdpdp.estimasiongkir.GetRateEstimationUseCase;

import dagger.Module;
import dagger.Provides;

@Module
@RatesEstimationScope
public class RatesEstimationModule {

    @RatesEstimationScope
    @Provides
    GraphqlUseCase provideGraphqlUseCase(){
        return new GraphqlUseCase();
    }

    @RatesEstimationScope
    @Provides
    GetRateEstimationUseCase provideGetRateEstimationUseCase(GraphqlUseCase graphqlUseCase,
                                                             @ApplicationContext Context context){
        return new GetRateEstimationUseCase(graphqlUseCase, context);
    }
}
