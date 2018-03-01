package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.shop.product.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.product.data.source.cloud.model.PagingListOther;
import com.tokopedia.shop.product.domain.model.ShopEtalaseRequestModel;
import com.tokopedia.shop.product.domain.repository.ShopProductRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class GetShopEtalaseUseCase extends UseCase<PagingListOther<EtalaseModel>> {

    public static final String SHOP_ETALASE_REQUEST_MODEL = "SHOP_ETALASE_REQUEST_MODEL";
    private ShopProductRepository shopProductRepository;

    @Inject
    public GetShopEtalaseUseCase(ShopProductRepository shopProductRepository) {
        this.shopProductRepository = shopProductRepository;
    }

    @Override
    public Observable<PagingListOther<EtalaseModel>> createObservable(RequestParams requestParams) {
        return shopProductRepository.getShopEtalaseList((ShopEtalaseRequestModel) requestParams.getObject(SHOP_ETALASE_REQUEST_MODEL));
    }

    public static RequestParams createParams(ShopEtalaseRequestModel shopEtalaseRequestModel){
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(SHOP_ETALASE_REQUEST_MODEL, shopEtalaseRequestModel);
        return requestParams;
    }
}
