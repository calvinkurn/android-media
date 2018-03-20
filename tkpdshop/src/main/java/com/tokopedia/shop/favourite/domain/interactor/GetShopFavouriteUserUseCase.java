package com.tokopedia.shop.favourite.domain.interactor;

import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouritePagingList;
import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouriteUser;
import com.tokopedia.shop.favourite.domain.model.ShopFavouriteRequestModel;
import com.tokopedia.shop.favourite.domain.repository.ShopFavouriteRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class GetShopFavouriteUserUseCase extends UseCase<ShopFavouritePagingList<ShopFavouriteUser>> {

    private static final String SHOP_FAVOURITE_REQUEST_MODEL = "SHOP_FAVOURITE_REQUEST_MODEL";

    private ShopFavouriteRepository shopFavouriteRepository;

    @Inject
    public GetShopFavouriteUserUseCase(ShopFavouriteRepository shopNoteRepository) {
        this.shopFavouriteRepository = shopNoteRepository;
    }

    @Override
    public Observable<ShopFavouritePagingList<ShopFavouriteUser>> createObservable(RequestParams requestParams) {
        ShopFavouriteRequestModel shopFavouriteRequestModel = (ShopFavouriteRequestModel) requestParams.getObject(SHOP_FAVOURITE_REQUEST_MODEL);
        return shopFavouriteRepository.getShopFavouriteUserList(shopFavouriteRequestModel);
    }

    public static RequestParams createRequestParam(ShopFavouriteRequestModel shopFavouriteRequestModel) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(SHOP_FAVOURITE_REQUEST_MODEL, shopFavouriteRequestModel);
        return requestParams;
    }
}
