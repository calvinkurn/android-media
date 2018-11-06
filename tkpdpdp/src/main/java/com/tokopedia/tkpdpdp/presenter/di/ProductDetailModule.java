package com.tokopedia.tkpdpdp.presenter.di;

import android.content.Context;

import com.tokopedia.affiliatecommon.domain.GetProductAffiliateGqlUseCase;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tkpdpdp.domain.GetWishlistCountUseCase;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {ApiModule.class}
)
public class ProductDetailModule {

    @Provides
    GetWishlistCountUseCase provideGetWishlistCountUseCase(
            @MojitoQualifier MojitoApi mojitoApi
    ){
        return new GetWishlistCountUseCase(mojitoApi);
    }

    @Provides
    GetProductAffiliateGqlUseCase getProductAffiliateGqlUseCase(@ApplicationContext Context context,
                                                                GraphqlUseCase graphqlUseCase) {
        return new GetProductAffiliateGqlUseCase(context.getResources(), graphqlUseCase);
    }

    @Provides
    GraphqlUseCase graphqlUseCase() {
        return new GraphqlUseCase();
    }

    @Provides
    UserSession userSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
