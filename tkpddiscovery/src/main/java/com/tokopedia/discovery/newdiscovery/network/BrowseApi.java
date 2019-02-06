package com.tokopedia.discovery.newdiscovery.network;

import com.tokopedia.discovery.newdiscovery.constant.DiscoveryBaseURL;
import com.tokopedia.discovery.search.domain.model.SearchResponse;

import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface BrowseApi {
    @GET(DiscoveryBaseURL.Ace.PATH_UNIVERSE_SEARCH_V8)
    Observable<Response<SearchResponse>> getUniverseAutoCompleteV8(
            @QueryMap HashMap<String, Object> param
    );

    @DELETE(DiscoveryBaseURL.Ace.PATH_DELETE_SEARCH)
    Observable<Response<Void>> deleteRecentSearch(
            @QueryMap HashMap<String, Object> parameters
    );
}
