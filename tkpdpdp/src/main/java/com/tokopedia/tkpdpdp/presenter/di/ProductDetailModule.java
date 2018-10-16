package com.tokopedia.tkpdpdp.presenter.di;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.domain.GetProductAffiliateGqlUseCase;
import com.tokopedia.tkpdpdp.domain.GetWishlistCountUseCase;
import com.tokopedia.tkpdpdp.presenter.di.scope.QueryProductAffiliate;
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
    GetProductAffiliateGqlUseCase getProductAffiliateGqlUseCase(GraphqlUseCase graphqlUseCase,
                                                                UserSession userSession,
                                                                @QueryProductAffiliate String queryProductAffiliate) {
        return new GetProductAffiliateGqlUseCase(graphqlUseCase, userSession, queryProductAffiliate);
    }

    @Provides
    GraphqlUseCase graphqlUseCase() {
        return new GraphqlUseCase();
    }

    @Provides
    UserSession userSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @QueryProductAffiliate
    @Provides
    String queryProductAffiliate(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_product_affiliate_data);
    }
}
