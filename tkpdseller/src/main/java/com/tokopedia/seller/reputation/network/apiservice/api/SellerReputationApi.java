package com.tokopedia.seller.reputation.network.apiservice.api;

import com.tokopedia.seller.reputation.constant.ReputationNetworkConstant;
import com.tokopedia.seller.reputation.model.response.SellerReputationResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author vishal.gupta on 20/02/2017.
 */
public interface SellerReputationApi {

    @GET(ReputationNetworkConstant.URL_REPUTATION + ReputationNetworkConstant.PATH_PENALTY)
    Observable<Response<SellerReputationResponse>> getReputationHistory(@Path("shop_id") String shopId, @QueryMap Map<String, String> params);
}
