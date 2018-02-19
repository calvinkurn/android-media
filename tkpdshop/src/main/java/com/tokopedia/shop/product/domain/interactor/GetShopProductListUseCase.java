package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.shop.product.data.source.cloud.model.ShopProductList;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.domain.repository.ShopProductRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class GetShopProductListUseCase extends UseCase<ShopProductList> {

    private final static String SHOP_REQUEST = "SHOP_REQUEST";

    private final ShopProductRepository shopNoteRepository;

    @Inject
    public GetShopProductListUseCase(ShopProductRepository shopNoteRepository) {
        super();
        this.shopNoteRepository = shopNoteRepository;
    }

    @Override
    public Observable<ShopProductList> createObservable(RequestParams requestParams) {
        ShopProductRequestModel shopProductRequestModel = (ShopProductRequestModel) requestParams.getObject(SHOP_REQUEST);
        return shopNoteRepository.getShopProductList(shopProductRequestModel);
    }

    public static RequestParams createRequestParam(ShopProductRequestModel shopProductRequestModel) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(SHOP_REQUEST, shopProductRequestModel);
        return requestParams;
    }
}
