package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.wishlist.common.domain.interactor.GetWishListUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class GetShopProductListWithAttributeUseCase extends UseCase<PagingList<ShopProductViewModel>> {

    private final static String SHOP_REQUEST = "SHOP_REQUEST";

    private final GetShopProductListUseCase getShopProductListUseCase;
    private final GetWishListUseCase getWishListUseCase;
    private final UserSession userSession;
    private final ShopProductMapper shopProductMapper;
    private final GetProductCampaignsUseCase getProductCampaignsUseCase;

    @Inject
    public GetShopProductListWithAttributeUseCase(GetShopProductListUseCase getShopProductListUseCase,
                                                  GetWishListUseCase getWishListUseCase,
                                                  GetProductCampaignsUseCase getProductCampaignsUseCase,
                                                  UserSession userSession,
                                                  ShopProductMapper shopProductMapper) {
        this.getShopProductListUseCase = getShopProductListUseCase;
        this.getWishListUseCase = getWishListUseCase;
        this.getProductCampaignsUseCase = getProductCampaignsUseCase;
        this.userSession = userSession;
        this.shopProductMapper = shopProductMapper;
    }

    public static RequestParams createRequestParam(ShopProductRequestModel shopProductRequestModel) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(SHOP_REQUEST, shopProductRequestModel);
        return requestParams;
    }

    @Override
    public Observable<PagingList<ShopProductViewModel>> createObservable(RequestParams requestParams) {
        final ShopProductRequestModel shopProductRequestModel = (ShopProductRequestModel) requestParams.getObject(SHOP_REQUEST);
        return getShopProductListUseCase.createObservable(GetShopProductListUseCase.createRequestParam(shopProductRequestModel)).flatMap(new Func1<PagingList<ShopProduct>, Observable<PagingList<ShopProductViewModel>>>() {
            @Override
            public Observable<PagingList<ShopProductViewModel>> call(final PagingList<ShopProduct> shopProductPagingList) {
                return getShopProductViewModelList(shopProductRequestModel, shopProductPagingList);
            }
        });
    }

    private Observable<PagingList<ShopProductViewModel>> getShopProductViewModelList(
            final ShopProductRequestModel shopProductRequestModel, final PagingList<ShopProduct> shopProductPagingList) {
        List<String> defaultWishList = new ArrayList<>();
        List<ShopProductCampaign> defaultShopProductCampaignList = new ArrayList<>();
        Observable<List<String>> wishlistObservable = Observable.just(defaultWishList);
        Observable<List<ShopProductCampaign>> campaignListObservable = Observable.just(defaultShopProductCampaignList);

        final List<String> productIdList = new ArrayList<>();
        for (ShopProduct shopProduct : shopProductPagingList.getList()) {
            productIdList.add(shopProduct.getProductId());
        }
        if (!isShopOwner(shopProductRequestModel.getShopId())) {
            wishlistObservable = getWishListUseCase.createObservable(GetWishListUseCase.createRequestParam(userSession.getUserId(), productIdList));
        }
        if (shopProductRequestModel.isOfficialStore()) {
            campaignListObservable = getProductCampaignsUseCase.createObservable(GetProductCampaignsUseCase.createRequestParam(productIdList));
        }

        return Observable.zip(wishlistObservable, campaignListObservable, new Func2<List<String>, List<ShopProductCampaign>, PagingList<ShopProductViewModel>>() {
            @Override
            public PagingList<ShopProductViewModel> call(List<String> wishList, List<ShopProductCampaign> shopProductCampaignList) {
                List<ShopProductViewModel> shopProductViewModelList = shopProductMapper.convertFromShopProduct(shopProductPagingList.getList());
                PagingList<ShopProductViewModel> pagingList = new PagingList<>();
                pagingList.setTotalData(shopProductPagingList.getTotalData());
                pagingList.setPaging(shopProductPagingList.getPaging());
                pagingList.setList(shopProductViewModelList);
                shopProductMapper.mergeShopProductViewModelWithWishList(shopProductViewModelList, wishList, !isShopOwner(shopProductRequestModel.getShopId()));
                shopProductMapper.mergeShopProductViewModelWithProductCampaigns(shopProductViewModelList, shopProductCampaignList);
                return pagingList;
            }
        });
    }

    private boolean isShopOwner(String shopId) {
        return userSession.getShopId().equals(shopId);
    }
}
