package com.tokopedia.discovery.newdiscovery.helper;

import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class GqlSearchHelper {
    private static final String KEY_QUERY = "query";
    private static final String KEY_PARAMS = "params";
    private static final String KEY_SOURCE = "source";
    private static final String KEY_HEADLINE_PARAMS = "headline_params";

    public static void initiateSearch(String query,
                                      com.tokopedia.usecase.RequestParams requestParams,
                                      GraphqlUseCase graphqlUseCase,
                                      Subscriber<GraphqlResponse> subscriber) {

        Map<String, Object> variables = new HashMap<>();
        variables.put(KEY_PARAMS, UrlParamHelper.generateUrlParamString(requestParams.getParamsAllValueInString()));

        GraphqlRequest graphqlRequest = new GraphqlRequest(query, InitiateSearchModel.class, variables);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(subscriber);
    }
}
