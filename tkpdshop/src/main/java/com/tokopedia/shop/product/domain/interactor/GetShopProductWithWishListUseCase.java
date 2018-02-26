package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
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

/**
 * Created by normansyahputa on 2/23/18.
 */

public class GetShopProductWithWishListUseCase extends UseCase<PagingList<ShopProductViewModel>> {

    private final static String SHOP_REQUEST = "SHOP_REQUEST";

    private final GetShopProductListUseCase getShopProductListUseCase;
    private final GetWishListUseCase getWishListUseCase;
    private final UserSession userSession;
    private final ShopProductMapper shopProductMapper;

    @Inject
    public GetShopProductWithWishListUseCase(GetShopProductListUseCase getShopProductListUseCase,
                                             GetWishListUseCase getWishListUseCase,
                                             UserSession userSession,
                                             ShopProductMapper shopProductMapper) {
        this.getShopProductListUseCase = getShopProductListUseCase;
        this.getWishListUseCase = getWishListUseCase;
        this.userSession = userSession;
        this.shopProductMapper = shopProductMapper;
    }

    @Override
    public Observable<PagingList<ShopProductViewModel>> createObservable(RequestParams requestParams) {
        final ShopProductRequestModel shopProductRequestModel = (ShopProductRequestModel) requestParams.getObject(SHOP_REQUEST);
        return getShopProductListUseCase.createObservable(GetShopProductListUseCase.createRequestParam(shopProductRequestModel)).flatMap(new Func1<PagingList<ShopProduct>, Observable<PagingList<ShopProductViewModel>>>() {
            @Override
            public Observable<PagingList<ShopProductViewModel>> call(final PagingList<ShopProduct> shopProductPagingList) {
                List<String> productIdList = new ArrayList<>();
                for (ShopProduct shopProduct : shopProductPagingList.getList()) {
                    productIdList.add(shopProduct.getProductId());
                }
                return getWishListUseCase.createObservable(GetWishListUseCase.createRequestParam(userSession.getUserId(), productIdList)).flatMap(new Func1<List<String>, Observable<PagingList<ShopProductViewModel>>>() {
                    @Override
                    public Observable<PagingList<ShopProductViewModel>> call(List<String> productWishList) {
                        PagingList<ShopProductViewModel> pagingList = new PagingList<>();
                        pagingList.setTotalData(shopProductPagingList.getTotalData());
                        pagingList.setList(shopProductMapper.convertFromShopProduct(shopProductPagingList.getList(), productWishList));
                        return Observable.just(pagingList);
                    }
                });
            }
        });
    }

    public static RequestParams createRequestParam(ShopProductRequestModel shopProductRequestModel) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(SHOP_REQUEST, shopProductRequestModel);
        return requestParams;
    }
}
