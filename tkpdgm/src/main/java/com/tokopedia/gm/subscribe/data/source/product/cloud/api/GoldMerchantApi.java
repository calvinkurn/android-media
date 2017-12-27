package com.tokopedia.gm.subscribe.data.source.product.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.gm.subscribe.data.source.product.cloud.model.GmServiceModel;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public interface GoldMerchantApi {

    @GET(TkpdBaseURL.GoldMerchant.GET_GM_SUBSCRIBE_PRODUCT)
    Observable<Response<GmServiceModel>> getGoldMerchantProductList();
}
