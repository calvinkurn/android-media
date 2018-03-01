package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.common.constant.ShopStatusDef;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.domain.repository.ShopProductRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class GetShopProductListUseCase extends UseCase<PagingList<ShopProduct>> {

    private final static String SHOP_REQUEST = "SHOP_REQUEST";

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final ShopProductRepository shopNoteRepository;

    @Inject
    public GetShopProductListUseCase(GetShopInfoUseCase getShopInfoUseCase, ShopProductRepository shopProductRepository) {
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.shopNoteRepository = shopProductRepository;
    }

    @Override
    public Observable<PagingList<ShopProduct>> createObservable(RequestParams requestParams) {
        final ShopProductRequestModel shopProductRequestModel = (ShopProductRequestModel) requestParams.getObject(SHOP_REQUEST);
        return getShopInfoUseCase.createObservable(GetShopInfoUseCase.createRequestParam(shopProductRequestModel.getShopId())).flatMap(new Func1<ShopInfo, Observable<PagingList<ShopProduct>>>() {
            @Override
            public Observable<PagingList<ShopProduct>> call(ShopInfo shopInfo) {
                shopProductRequestModel.setShopClosed((int) shopInfo.getInfo().getShopStatus() == ShopStatusDef.CLOSED);
                return shopNoteRepository.getShopProductList(shopProductRequestModel);
            }
        });
    }

    public static RequestParams createRequestParam(ShopProductRequestModel shopProductRequestModel) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(SHOP_REQUEST, shopProductRequestModel);
        return requestParams;
    }
}
