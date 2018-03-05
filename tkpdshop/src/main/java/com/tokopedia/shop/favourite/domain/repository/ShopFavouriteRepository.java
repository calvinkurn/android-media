package com.tokopedia.shop.favourite.domain.repository;

import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouritePagingList;
import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouriteUser;
import com.tokopedia.shop.favourite.domain.model.ShopFavouriteRequestModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface ShopFavouriteRepository {

    Observable<ShopFavouritePagingList<ShopFavouriteUser>> getShopFavouriteUserList(ShopFavouriteRequestModel shopFavouriteRequestModel);

}
