package com.tokopedia.wishlist.common.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.wishlist.common.data.source.cloud.api.WishListCommonApi;
import com.tokopedia.wishlist.common.data.source.cloud.mapper.WishListProductListMapper;
import com.tokopedia.wishlist.common.data.source.cloud.model.WishListData;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author hendry on 4/4/17.
 */

public class WishListCommonCloudDataSource {

    private final WishListCommonApi wishListCommonApi;
    private final WishListProductListMapper wishListProductListMapper;

    public WishListCommonCloudDataSource(WishListCommonApi reputationCommonApi, WishListProductListMapper wishListProductListMapper) {
        this.wishListCommonApi = reputationCommonApi;
        this.wishListProductListMapper = wishListProductListMapper;
    }

    public Observable<Response<DataResponse<WishListData>>> getWishList(String userId, List<String> productIdList) {
        return wishListCommonApi.getWishList(userId, wishListProductListMapper.convertCommaValue(productIdList));
    }

    public Observable<Boolean> addToWishList(String userId, String productId) {
        return wishListCommonApi.addToWishList(userId, productId).flatMap(new Func1<Response<DataResponse<Void>>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<DataResponse<Void>> dataResponseResponse) {
                return Observable.just(true);
            }
        });
    }

    public Observable<Boolean> removeFromWishList(String userId, String productId) {
        return wishListCommonApi.removeFromWishList(userId, productId).flatMap(new Func1<Response<DataResponse<Void>>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Response<DataResponse<Void>> dataResponseResponse) {
                return Observable.just(true);
            }
        });
    }
}
