package com.tokopedia.shop.favourite.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouritePagingList;
import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouriteUser;
import com.tokopedia.shop.favourite.domain.interactor.GetShopFavouriteUserUseCase;
import com.tokopedia.shop.favourite.domain.model.ShopFavouriteRequestModel;
import com.tokopedia.shop.favourite.view.listener.ShopFavouriteListView;
import com.tokopedia.shop.favourite.view.mapper.ShopFavouriteViewModelMapper;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopFavouriteListPresenter extends BaseDaggerPresenter<ShopFavouriteListView> {

    private static final int SHOP_FAVOURITE_PER_PAGE = 12;

    private final GetShopFavouriteUserUseCase getShopFavouriteUserUseCase;
    private final ShopFavouriteViewModelMapper shopFavouriteViewModelMapper;

    @Inject
    public ShopFavouriteListPresenter(GetShopFavouriteUserUseCase getShopFavouriteUserUseCase,
                                      ShopFavouriteViewModelMapper shopFavouriteViewModelMapper) {
        this.getShopFavouriteUserUseCase = getShopFavouriteUserUseCase;
        this.shopFavouriteViewModelMapper = shopFavouriteViewModelMapper;
    }

    public void getshopFavouriteList(String shopId, int page) {
        ShopFavouriteRequestModel shopFavouriteRequestModel = new ShopFavouriteRequestModel();
        shopFavouriteRequestModel.setShopId(shopId);
        shopFavouriteRequestModel.setPage(page);
        shopFavouriteRequestModel.setPerPage(SHOP_FAVOURITE_PER_PAGE);
        getShopFavouriteUserUseCase.execute(GetShopFavouriteUserUseCase.createRequestParam(shopFavouriteRequestModel), new Subscriber<ShopFavouritePagingList<ShopFavouriteUser>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(ShopFavouritePagingList<ShopFavouriteUser> shopFavouriteUserPagingList) {
                getView().renderList(shopFavouriteViewModelMapper.transform(shopFavouriteUserPagingList.getList()),
                        shopFavouriteUserPagingList.getPage() < shopFavouriteUserPagingList.getTotalPage());
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopFavouriteUserUseCase.unsubscribe();
    }
}