package com.tokopedia.shop.favourite.view.mapper;

import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouriteUser;
import com.tokopedia.shop.favourite.view.model.ShopFavouriteViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/14/17.
 */

public class ShopFavouriteViewModelMapper {

    @Inject
    public ShopFavouriteViewModelMapper() {
    }

    public List<ShopFavouriteViewModel> transform(List<ShopFavouriteUser> shopFavouriteViewModelList) {
        List<ShopFavouriteViewModel> favouriteViewModelList = new ArrayList<>();
        for (ShopFavouriteUser shopFavouriteUser : shopFavouriteViewModelList) {
            ShopFavouriteViewModel shopFavouriteViewModel = new ShopFavouriteViewModel();
            shopFavouriteViewModel.setId(shopFavouriteUser.getUserId());
            shopFavouriteViewModel.setName(shopFavouriteUser.getName());
            shopFavouriteViewModel.setImageUrl(shopFavouriteUser.getImageUrl());
            favouriteViewModelList.add(shopFavouriteViewModel);
        }
        return favouriteViewModelList;
    }
}
