package com.tokopedia.seller.shopscore.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public interface ShopScoreApi {

    @GET(TkpdBaseURL.GoldMerchant.GET_SHOP_SCORE_SUMMARY + "{shopId}")
    Observable<String> getShopScoreSummary(@Path("shopId") String shopId);

    @GET(TkpdBaseURL.GoldMerchant.GET_SHOP_SCORE_SUMMARY + "{shopId}")
    Observable<String> getShopScoreDetail(@Path("shopId") String shopID);
}
