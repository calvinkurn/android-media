package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedProductViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class GetShopProductLimitedUseCase extends UseCase<List<ShopProductBaseViewModel>> {

    private static final String SHOP_ID = "SHOP_ID";

    private final GetShopProductFeaturedUseCase getShopProductFeaturedUseCase;
    private final GetShopProductWithWishListUseCase getShopProductWithWishListUseCase;

    @Inject
    public GetShopProductLimitedUseCase(GetShopProductFeaturedUseCase getShopProductFeaturedUseCase,
                                        GetShopProductWithWishListUseCase getShopProductWithWishListUseCase) {
        this.getShopProductFeaturedUseCase = getShopProductFeaturedUseCase;
        this.getShopProductWithWishListUseCase = getShopProductWithWishListUseCase;
    }

    @Override
    public Observable<List<ShopProductBaseViewModel>> createObservable(RequestParams requestParams) {
        final String shopId = requestParams.getString(SHOP_ID, "");
        final ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel();
        shopProductRequestModel.setShopId(shopId);
        return Observable.zip(
                getShopProductFeaturedUseCase.createObservable(GetShopProductFeaturedUseCase.createRequestParam(shopId)).subscribeOn(Schedulers.io()),
                getShopProductWithWishListUseCase.createObservable(GetShopProductListUseCase.createRequestParam(shopProductRequestModel)).subscribeOn(Schedulers.io()),
                new Func2<List<ShopProductViewModel>, PagingList<ShopProductViewModel>, List<ShopProductBaseViewModel>>() {
                    @Override
                    public List<ShopProductBaseViewModel> call(List<ShopProductViewModel> shopProductViewModelList, PagingList<ShopProductViewModel> shopProductList) {
                        List<ShopProductBaseViewModel> shopProductBaseViewModelList = new ArrayList<>();
                        if (shopProductViewModelList.size() > 0) {
                            ShopProductLimitedFeaturedViewModel shopProductLimitedFeaturedViewModel = new ShopProductLimitedFeaturedViewModel();
                            shopProductLimitedFeaturedViewModel.setShopProductViewModelList(shopProductViewModelList);
                            shopProductBaseViewModelList.add(shopProductLimitedFeaturedViewModel);
                        }
                        if (shopProductList.getList().size() > 0) {
                            ShopProductLimitedProductViewModel shopProductLimitedProductViewModel = new ShopProductLimitedProductViewModel();
                            shopProductLimitedProductViewModel.setShopProductViewModelList(shopProductList.getList());
                            shopProductBaseViewModelList.add(shopProductLimitedProductViewModel);
                        }
                        return shopProductBaseViewModelList;
                    }
                }
        );
    }

    public static RequestParams createRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }
}
