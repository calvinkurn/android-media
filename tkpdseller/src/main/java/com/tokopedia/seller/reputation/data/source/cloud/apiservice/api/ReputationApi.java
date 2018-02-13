package com.tokopedia.seller.reputation.data.source.cloud.apiservice.api;

import com.tokopedia.reputation.speed.SpeedReputation;
import com.tokopedia.seller.reputation.constant.ReputationNetworkConstant;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ReputationApi {
    @GET(ReputationNetworkConstant.URL_REPUTATION_SPEED)
    Observable<Response<SpeedReputation>> getReputationSpeed(@Path("shop_id") String shopId);
}
