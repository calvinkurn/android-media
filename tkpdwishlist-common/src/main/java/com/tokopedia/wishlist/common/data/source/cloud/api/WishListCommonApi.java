package com.tokopedia.wishlist.common.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.wishlist.common.constant.WishListCommonUrl;
import com.tokopedia.wishlist.common.constant.WishListParamApiContant;
import com.tokopedia.wishlist.common.data.source.cloud.model.WishListData;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface WishListCommonApi {

    @GET(WishListCommonUrl.GET_WISHLIST_URL)
    Observable<Response<DataResponse<WishListData>>> getWishList(@Path(WishListParamApiContant.USER_ID) String userId, @Path(WishListParamApiContant.PRODUCT_ID_LIST) String productIdList);

    @POST(WishListCommonUrl.SET_WISHLIST_URL)
    Observable<Response<DataResponse<Void>>> addToWishList(@Path(WishListParamApiContant.USER_ID) String userId, @Path(WishListParamApiContant.PRODUCT_ID) String productId);

    @DELETE(WishListCommonUrl.SET_WISHLIST_URL)
    Observable<Response<DataResponse<Void>>> removeFromWishList(@Path(WishListParamApiContant.USER_ID) String userId, @Path(WishListParamApiContant.PRODUCT_ID) String product);

}
