package com.tokopedia.wishlist.common.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.wishlist.common.data.source.cloud.WishListCommonCloudDataSource;
import com.tokopedia.wishlist.common.data.source.cloud.model.ShopProductCampaign;
import com.tokopedia.wishlist.common.data.source.cloud.model.WishListData;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class WishListCommonDataSource {

    private WishListCommonCloudDataSource wishListCommonCloudDataSource;

    public WishListCommonDataSource(WishListCommonCloudDataSource wishListCommonCloudDataSource) {
        this.wishListCommonCloudDataSource = wishListCommonCloudDataSource;
    }

    public Observable<List<String>> getWishList(String userId, List<String> productIdList) {
        return wishListCommonCloudDataSource.getWishList(userId, productIdList).flatMap(new Func1<Response<DataResponse<WishListData>>, Observable<List<String>>>() {
            @Override
            public Observable<List<String>> call(Response<DataResponse<WishListData>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData().getIds());
            }
        });
    }

    public Observable<Boolean> addToWishList(String userId, String productId) {
        return wishListCommonCloudDataSource.addToWishList(userId, productId);
    }

    public Observable<Boolean> removeFromWishList(String userId, String productId) {
        return wishListCommonCloudDataSource.removeFromWishList(userId, productId);
    }

    public Observable<List<ShopProductCampaign>> getProductCampaigns(String ids) {
        return wishListCommonCloudDataSource.getProductCampaigns(ids);
    }
}
