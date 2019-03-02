package com.tokopedia.discovery.newdiscovery.helper;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryPresenter;
import com.tokopedia.discovery.newdiscovery.domain.gql.SearchProductGqlResponse;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.topads.sdk.domain.TopAdsParams;


import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class GqlSearchHelper {
    private static final String KEY_QUERY = "query";
    private static final String KEY_PARAMS = "params";
    private static final String KEY_SOURCE = "source";
    private static final String KEY_HEADLINE_PARAMS = "headline_params";

    public static void requestProductFirstPage(Context context,
                                               RequestParams requestParams,
                                               GraphqlUseCase graphqlUseCase,
                                               Subscriber<GraphqlResponse> subscriber) {

        Map<String, Object> variables = new HashMap<>();
        variables.put(KEY_QUERY, requestParams.getString(BrowseApi.Q, ""));
        variables.put(KEY_PARAMS, UrlParamHelper.generateUrlParamString(requestParams.getParamsAllValueInString()));
        variables.put(KEY_SOURCE, requestParams.getString(BrowseApi.SOURCE, ""));

        TKPDMapParam<String, String> headlineParams = requestParams.getParamsAllValueInString();
        headlineParams.put(TopAdsParams.KEY_EP, DiscoveryPresenter.HEADLINE);
        headlineParams.put(TopAdsParams.KEY_TEMPLATE_ID, DiscoveryPresenter.TEMPLATE_VALUE);
        headlineParams.put(TopAdsParams.KEY_ITEM, DiscoveryPresenter.ITEM_VALUE);
        variables.put(KEY_HEADLINE_PARAMS, UrlParamHelper.generateUrlParamString(headlineParams));

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.gql_search_product_first_page), SearchProductGqlResponse.class, variables);

        graphqlUseCase.clearRequest();
        graphqlUseCase.setRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }

    public static void requestProductLoadMore(Context context,
                                               RequestParams requestParams,
                                               GraphqlUseCase graphqlUseCase,
                                               Subscriber<GraphqlResponse> subscriber) {

        Map<String, Object> variables = new HashMap<>();
        variables.put(KEY_PARAMS, UrlParamHelper.generateUrlParamString(requestParams.getParamsAllValueInString()));

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(context.getResources(),
                R.raw.gql_search_product), SearchProductGqlResponse.class, variables);

        graphqlUseCase.clearRequest();
        graphqlUseCase.setRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }
}
