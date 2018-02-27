package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.gm.common.domain.interactor.GetFeatureProductListUseCase;
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

/**
 * Created by normansyahputa on 2/23/18.
 */

public class GetShopProductFeaturedUseCase extends UseCase<List<ShopProductViewModel>> {

    private static final String SHOP_ID = "SHOP_ID";

    private final GetFeatureProductListUseCase getFeatureProductListUseCase;
    private final GetWishListUseCase getWishListUseCase;
    private final UserSession userSession;
    private final ShopProductMapper shopProductMapper;

    @Inject
    public GetShopProductFeaturedUseCase(GetFeatureProductListUseCase getFeatureProductListUseCase,
                                         GetWishListUseCase getWishListUseCase,
                                         UserSession userSession,
                                         ShopProductMapper shopProductMapper) {
        this.getFeatureProductListUseCase = getFeatureProductListUseCase;
        this.getWishListUseCase = getWishListUseCase;
        this.userSession = userSession;
        this.shopProductMapper = shopProductMapper;
    }

    @Override
    public Observable<List<ShopProductViewModel>> createObservable(RequestParams requestParams) {
        final String shopId = requestParams.getString(SHOP_ID, "");
        return getFeatureProductListUseCase.createObservable(GetFeatureProductListUseCase.createRequestParam(shopId)).flatMap(new Func1<List<GMFeaturedProduct>, Observable<List<ShopProductViewModel>>>() {
            @Override
            public Observable<List<ShopProductViewModel>> call(final List<GMFeaturedProduct> gmFeaturedProductList) {
                if (gmFeaturedProductList.size() > 0) {
                    List<String> productIdList = new ArrayList<>();
                    for (GMFeaturedProduct gmFeaturedProduct : gmFeaturedProductList) {
                        productIdList.add(gmFeaturedProduct.getProductId());
                    }
                    return getWishListUseCase.createObservable(GetWishListUseCase.createRequestParam(userSession.getUserId(), productIdList)).flatMap(new Func1<List<String>, Observable<List<ShopProductViewModel>>>() {
                        @Override
                        public Observable<List<ShopProductViewModel>> call(List<String> productWishList) {
                            return Observable.just(shopProductMapper.convertFromProductFeatured(gmFeaturedProductList, productWishList));
                        }
                    });
                } else {
                    return Observable.just(shopProductMapper.convertFromProductFeatured(gmFeaturedProductList, new ArrayList<String>()));
                }
            }
        });
    }

    public static RequestParams createRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }
}
