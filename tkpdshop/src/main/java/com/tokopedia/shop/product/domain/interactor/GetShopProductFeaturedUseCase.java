package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.gm.common.domain.interactor.GetFeatureProductListUseCase;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.wishlist.common.domain.interactor.GetWishListUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class GetShopProductFeaturedUseCase extends UseCase<List<ShopProductFeaturedViewModel>> {

    private static final String SHOP_ID = "SHOP_ID";

    private final GetFeatureProductListUseCase getFeatureProductListUseCase;
    private final GetWishListUseCase getWishListUseCase;
    private final UserSession userSession;

    @Inject
    public GetShopProductFeaturedUseCase(GetFeatureProductListUseCase getFeatureProductListUseCase,
                                         GetWishListUseCase getWishListUseCase,
                                         UserSession userSession) {
        this.getFeatureProductListUseCase = getFeatureProductListUseCase;
        this.getWishListUseCase = getWishListUseCase;
        this.userSession = userSession;
    }

    @Override
    public Observable<List<ShopProductFeaturedViewModel>> createObservable(RequestParams requestParams) {
        final String shopId = requestParams.getString(SHOP_ID, "");
        return getFeatureProductListUseCase.createObservable(GetFeatureProductListUseCase.createRequestParam(shopId)).flatMap(new Func1<List<GMFeaturedProduct>, Observable<List<ShopProductFeaturedViewModel>>>() {
            @Override
            public Observable<List<ShopProductFeaturedViewModel>> call(final List<GMFeaturedProduct> gmFeaturedProductList) {
                List<String> productIdList = new ArrayList<>();
                for (GMFeaturedProduct gmFeaturedProduct : gmFeaturedProductList) {
                    productIdList.add(gmFeaturedProduct.getProductId());
                }
                return getWishListUseCase.createObservable(GetWishListUseCase.createRequestParam(userSession.getUserId(), productIdList)).flatMap(new Func1<List<String>, Observable<List<ShopProductFeaturedViewModel>>>() {
                    @Override
                    public Observable<List<ShopProductFeaturedViewModel>> call(List<String> productWishList) {
                        List<ShopProductFeaturedViewModel> shopPageFeaturedProductList = new ArrayList<>();
                        for (GMFeaturedProduct gmFeaturedProduct : gmFeaturedProductList) {
                            ShopProductFeaturedViewModel shopPageFeaturedProduct = new ShopProductFeaturedViewModel();
                            shopPageFeaturedProduct.setGmFeaturedProduct(gmFeaturedProduct);
                            shopPageFeaturedProduct.setWhisList(isWishList(gmFeaturedProduct.getProductId(), productWishList));
                            shopPageFeaturedProductList.add(shopPageFeaturedProduct);
                        }
                        return Observable.just(shopPageFeaturedProductList);
                    }

                    private boolean isWishList(String productId, List<String> productWishList) {
                        for (String productWishListId : productWishList) {
                            if (productId.equalsIgnoreCase(productWishListId)) {
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }
        });
    }

    public static RequestParams createRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }
}
