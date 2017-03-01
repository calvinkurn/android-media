package com.tokopedia.seller.shopscore.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.shopscore.data.source.cloud.model.detail.ShopScoreDetailServiceModel;
import com.tokopedia.seller.shopscore.data.source.cloud.model.summary.ShopScoreSummaryServiceModel;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public interface ShopScoreApi {

    @GET(TkpdBaseURL.GoldMerchant.GET_SHOP_SCORE_SUMMARY + "{shopId}")
    Observable<Response<ShopScoreSummaryServiceModel>> getShopScoreSummary(@Path("shopId") String shopId);

    @GET(TkpdBaseURL.GoldMerchant.GET_SHOP_SCORE_DETAIL + "{shopId}")
    Observable<Response<ShopScoreDetailServiceModel>> getShopScoreDetail(@Path("shopId") String shopID);

}
