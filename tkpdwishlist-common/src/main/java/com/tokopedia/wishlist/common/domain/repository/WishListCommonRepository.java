package com.tokopedia.wishlist.common.domain.repository;

import com.tokopedia.wishlist.common.data.source.cloud.model.ShopProductCampaignResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface WishListCommonRepository {

    Observable<List<String>> getWishList(String userId, List<String> productIdList);

    Observable<Boolean> addToWishList(String userId, String productId);

    Observable<Boolean> removeFromWishList(String userId, String productId);

    Observable<ShopProductCampaignResponse> getProductCampaigns(String ids);
}