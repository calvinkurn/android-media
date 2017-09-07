package com.tokopedia.seller.goldmerchant.featured.data.cloud.api;

import com.tokopedia.seller.goldmerchant.featured.constant.FeaturedConstant;
import com.tokopedia.seller.goldmerchant.featured.data.model.FeaturedProductGETModel;
import com.tokopedia.seller.goldmerchant.featured.data.model.PostFeaturedProductModel;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by normansyahputa on 9/6/17.
 */

public interface FeaturedProductApi {
    @GET(FeaturedConstant.GET_FEATURED_PRODUCT_URL)
    Observable<Response<FeaturedProductGETModel>> getFeaturedProduct(@Path("id") String shopId);

    @POST(FeaturedConstant.POST_FEATURED_PRODUCT_URL)
    Observable<Response<FeaturedProductGETModel>> postFeaturedProduct(@Path("id") String shopId,
                                                                      @Body PostFeaturedProductModel postFeaturedProductModel);
}
