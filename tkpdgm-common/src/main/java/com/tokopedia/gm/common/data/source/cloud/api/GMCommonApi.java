package com.tokopedia.gm.common.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.gm.common.constant.GMCommonUrl;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface GMCommonApi {

    @GET(GMCommonUrl.FEATURED_PRODUCT_URL)
    Observable<Response<DataResponse<List<GMFeaturedProduct>>>> getFeaturedProductList(@Path("shop_id") String shopId);

}
