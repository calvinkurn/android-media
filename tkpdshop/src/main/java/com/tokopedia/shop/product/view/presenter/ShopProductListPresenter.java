package com.tokopedia.shop.product.view.presenter;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote;
import com.tokopedia.shop.note.domain.interactor.GetShopNoteListUseCase;
import com.tokopedia.shop.note.view.listener.ShopNoteListView;
import com.tokopedia.shop.note.view.mapper.ShopNoteViewModelMapper;
import com.tokopedia.shop.product.data.source.cloud.model.Badge;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductList;
import com.tokopedia.shop.product.domain.interactor.GetShopProductListUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.listener.ShopProductListView;
import com.tokopedia.shop.product.view.mapper.ShopProductViewModelMapper;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListPresenter extends BaseDaggerPresenter<BaseListViewListener<ShopProductViewModel>> {

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final GetShopProductListUseCase getShopProductListUseCase;
    private final ShopProductViewModelMapper shopProductViewModelMapper;

    @Inject
    public ShopProductListPresenter(GetShopInfoUseCase getShopInfoUseCase, GetShopProductListUseCase getShopProductListUseCase, ShopProductViewModelMapper shopProductViewModelMapper) {
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.getShopProductListUseCase = getShopProductListUseCase;
        this.shopProductViewModelMapper = shopProductViewModelMapper;
    }

    public void getShopPageList(String shopId) {
        ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel();
        shopProductRequestModel.setShopId(shopId);
        shopProductRequestModel.setPage(1);
        getShopProductListUseCase.execute(GetShopProductListUseCase.createRequestParam(shopProductRequestModel), new Subscriber<ShopProductList>() {
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
            public void onNext(ShopProductList shopProductList) {
                if(!isViewAttached())
                    return;

                getView().renderList(convert(shopProductList.getList()));
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopProductListUseCase.unsubscribe();
    }

    public static List<ShopProductViewModel> convert(List<ShopProduct> shopProducts){
        List<ShopProductViewModel> shopProductViewModels = new ArrayList<>();
        for (ShopProduct shopProduct : shopProducts) {
            shopProductViewModels.add(convert(shopProduct));
        }
        return shopProductViewModels;
    }

    public static ShopProductViewModel convert(ShopProduct shopProducts){
        ShopProductViewModel shopProductViewModels = new ShopProductViewModel();
        List<ShopProductViewModel.Badge> badges = new ArrayList<>();
        for (Badge badge : shopProducts.getBadges()) {
            ShopProductViewModel.Badge badgeModel = new ShopProductViewModel.Badge();
            badgeModel.setImageUrl(badge.getImageUrl());
            badgeModel.setTitle(badge.getTitle());

            badges.add(badgeModel);
        }

        shopProductViewModels.setBadges(badges);
        shopProductViewModels.setLabels(shopProducts.getLabels());
        shopProductViewModels.setProductId(shopProducts.getProductId());
        shopProductViewModels.setShopId(shopProducts.getShopId());
        shopProductViewModels.setProductImage(shopProducts.getProductImage());
        shopProductViewModels.setProductImage300(shopProducts.getProductImage300());
        shopProductViewModels.setProductImage700(shopProducts.getProductImage700());
        shopProductViewModels.setProductName(shopProducts.getProductName());
        shopProductViewModels.setProductPreorder(shopProducts.getProductPreorder());
        shopProductViewModels.setProductPrice(shopProducts.getProductPrice());
        shopProductViewModels.setProductReviewCount(shopProducts.getProductReviewCount());
        shopProductViewModels.setProductTalkCount(shopProducts.getProductTalkCount());
        shopProductViewModels.setProductUrl(shopProducts.getProductUrl());
        shopProductViewModels.setProductWholesale(shopProducts.getProductWholesale());
        shopProductViewModels.setShopGoldStatus(shopProducts.getShopGoldStatus());
        shopProductViewModels.setShopLocation(shopProducts.getShopLocation());
        shopProductViewModels.setShopLucky(shopProducts.getShopLucky());
        shopProductViewModels.setShopName(shopProducts.getShopName());
        shopProductViewModels.setShopUrl(shopProducts.getShopUrl());

        return shopProductViewModels;
    }
}