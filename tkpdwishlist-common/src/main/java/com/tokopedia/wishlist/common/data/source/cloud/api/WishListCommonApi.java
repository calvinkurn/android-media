package com.tokopedia.wishlist.common.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.wishlist.common.constant.WishListCommonUrl;
import com.tokopedia.wishlist.common.constant.WishListParamApiConstant;
import com.tokopedia.wishlist.common.data.source.cloud.model.ShopProductCampaign;
import com.tokopedia.wishlist.common.data.source.cloud.model.WishListData;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface WishListCommonApi {

    @GET(WishListCommonUrl.GET_WISHLIST_URL)
    Observable<Response<DataResponse<WishListData>>> getWishList(@Path(WishListParamApiConstant.USER_ID) String userId,
                                                                 @Path(WishListParamApiConstant.PRODUCT_ID_LIST) String productIdList);

    @POST(WishListCommonUrl.SET_WISHLIST_URL)
    Observable<Response<Void>> addToWishList(@Path(WishListParamApiConstant.USER_ID) String userId,
                                             @Path(WishListParamApiConstant.PRODUCT_ID) String productId);

    @DELETE(WishListCommonUrl.SET_WISHLIST_URL)
    Observable<Response<Void>> removeFromWishList(@Path(WishListParamApiConstant.USER_ID) String userId,
                                                  @Path(WishListParamApiConstant.PRODUCT_ID) String productId);

    @GET("/os/v1/campaign/product/info")
    Observable<Response<DataResponse<List<ShopProductCampaign>>>> getProductCampaigns(@Query("pid") String ids);
}
