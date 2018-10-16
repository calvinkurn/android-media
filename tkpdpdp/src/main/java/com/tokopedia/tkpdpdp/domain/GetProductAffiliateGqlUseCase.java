package com.tokopedia.tkpdpdp.domain;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tkpdpdp.entity.TopAdsPdpAffiliateResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;

import rx.Observable;

public class GetProductAffiliateGqlUseCase extends UseCase<TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate> {

    private String queryProductAffiliate;
    private GraphqlUseCase graphqlUseCase;
    private UserSession userSession;

    public static final String USER_ID_PARAM = "userId";
    public static final String SHOP_ID_PARAM = "shopId";
    public static final String PRODUCT_ID_PARAM = "productId";

    public static final String PARAMS_GUEST_USER_ID = "0";
    public static final String PARAMS_DEFAULT_SHOP_ID = "1";
    public static final String PARAMS_DEFAULT_PRODUCT_ID = "1";

    public GetProductAffiliateGqlUseCase(GraphqlUseCase graphqlUseCase,
                                         UserSession userSession,
                                         String queryProductAffiliate) {
        this.userSession = userSession;
        this.graphqlUseCase = graphqlUseCase;
        this.queryProductAffiliate = queryProductAffiliate;
    }

    @Override
    public Observable<TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate> createObservable(RequestParams requestParams) {
        requestParams.putInt(USER_ID_PARAM,
                Integer.parseInt(userSession.isLoggedIn() ?
                        userSession.getUserId() : PARAMS_GUEST_USER_ID)
        );

        GraphqlRequest graphRequest = new GraphqlRequest(
                queryProductAffiliate,
                TopAdsPdpAffiliateResponse.class,
                requestParams.getParameters()
        );
        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphRequest);

        return graphqlUseCase
                .createObservable(null)
                .map(graphqlResponse -> {
                    TopAdsPdpAffiliateResponse response = graphqlResponse.getData(TopAdsPdpAffiliateResponse.class);
                    if (response != null && response.getTopAdsPDPAffiliate() != null) {
                        return response.getTopAdsPDPAffiliate();
                    } else {
                        return null;
                    }
                });
    }
}
