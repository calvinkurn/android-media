package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.gm.common.domain.interactor.GetFeatureProductListUseCase;
import com.tokopedia.shop.product.view.model.ShopPageFeaturedProduct;
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

public class GetShopPageFeaturedProductUseCase extends UseCase<List<ShopPageFeaturedProduct>> {

    private static final String SHOP_ID = "SHOP_ID";

    private final GetFeatureProductListUseCase getFeatureProductListUseCase;
    private final GetWishListUseCase getWishListUseCase;
    private final UserSession userSession;

    @Inject
    public GetShopPageFeaturedProductUseCase(GetFeatureProductListUseCase getFeatureProductListUseCase,
                                             GetWishListUseCase getWishListUseCase,
                                             UserSession userSession) {
        this.getFeatureProductListUseCase = getFeatureProductListUseCase;
        this.getWishListUseCase = getWishListUseCase;
        this.userSession = userSession;
    }

    @Override
    public Observable<List<ShopPageFeaturedProduct>> createObservable(RequestParams requestParams) {
        final String shopId = requestParams.getString(SHOP_ID, "");
        return getFeatureProductListUseCase.createObservable(GetFeatureProductListUseCase.createRequestParam(shopId)).flatMap(new Func1<List<GMFeaturedProduct>, Observable<List<ShopPageFeaturedProduct>>>() {
            @Override
            public Observable<List<ShopPageFeaturedProduct>> call(final List<GMFeaturedProduct> gmFeaturedProductList) {
                List<String> productIdList = new ArrayList<>();
                for (GMFeaturedProduct gmFeaturedProduct : gmFeaturedProductList) {
                    productIdList.add(gmFeaturedProduct.getProductId());
                }
                return getWishListUseCase.createObservable(GetWishListUseCase.createRequestParam(userSession.getUserId(), productIdList)).flatMap(new Func1<List<String>, Observable<List<ShopPageFeaturedProduct>>>() {
                    @Override
                    public Observable<List<ShopPageFeaturedProduct>> call(List<String> productWishList) {
                        List<ShopPageFeaturedProduct> shopPageFeaturedProductList = new ArrayList<>();
                        for (GMFeaturedProduct gmFeaturedProduct : gmFeaturedProductList) {
                            ShopPageFeaturedProduct shopPageFeaturedProduct = new ShopPageFeaturedProduct();
                            shopPageFeaturedProduct.setGmFeaturedProduct(gmFeaturedProduct);
                            shopPageFeaturedProduct.setWhislist(isWishList(gmFeaturedProduct.getProductId(), productWishList));
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
