package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.wishlist.common.domain.interactor.GetWishListUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 2/23/18.
 */

public abstract class GetShopProductAttributeUseCase<T> extends UseCase<T> {

    private final GetWishListUseCase getWishListUseCase;
    private final UserSession userSession;
    protected final ShopProductMapper shopProductMapper;
    private final GetProductCampaignsUseCase getProductCampaignsUseCase;

    public GetShopProductAttributeUseCase(GetWishListUseCase getWishListUseCase, GetProductCampaignsUseCase getProductCampaignsUseCase,
                                          UserSession userSession, ShopProductMapper shopProductMapper) {
        this.getWishListUseCase = getWishListUseCase;
        this.getProductCampaignsUseCase = getProductCampaignsUseCase;
        this.userSession = userSession;
        this.shopProductMapper = shopProductMapper;
    }

    protected Observable<List<ShopProductViewModel>> getShopProductViewModelList(
            final boolean isShopOwner, boolean officialStore, final List<ShopProductViewModel> shopProductViewModelList) {
        List<String> defaultWishList = new ArrayList<>();
        List<ShopProductCampaign> defaultShopProductCampaignList = new ArrayList<>();
        Observable<List<String>> wishlistObservable = Observable.just(defaultWishList);
        Observable<List<ShopProductCampaign>> campaignListObservable = Observable.just(defaultShopProductCampaignList);

        final List<String> productIdList = new ArrayList<>();
        for (ShopProductViewModel shopProductViewModel : shopProductViewModelList) {
            productIdList.add(shopProductViewModel.getId());
        }
        if (shopProductViewModelList.size() > 0 && !isShopOwner) {
            wishlistObservable = getWishListUseCase.createObservable(GetWishListUseCase.createRequestParam(userSession.getUserId(), productIdList));
        }
        if (shopProductViewModelList.size() > 0 && officialStore) {
            campaignListObservable = getProductCampaignsUseCase.createObservable(GetProductCampaignsUseCase.createRequestParam(productIdList));
        }

        return Observable.zip(wishlistObservable.subscribeOn(Schedulers.io()), campaignListObservable.subscribeOn(Schedulers.io()), new Func2<List<String>, List<ShopProductCampaign>, List<ShopProductViewModel>>() {
            @Override
            public List<ShopProductViewModel> call(List<String> wishList, List<ShopProductCampaign> shopProductCampaignList) {
                shopProductMapper.mergeShopProductViewModelWithWishList(shopProductViewModelList, wishList, !isShopOwner);
                shopProductMapper.mergeShopProductViewModelWithProductCampaigns(shopProductViewModelList, shopProductCampaignList);
                return shopProductViewModelList;
            }
        });
    }

    protected boolean isShopOwner(String shopId) {
        return userSession.getShopId().equals(shopId);
    }
}
