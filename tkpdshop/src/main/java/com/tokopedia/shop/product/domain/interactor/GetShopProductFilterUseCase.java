package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.shop.product.data.source.cloud.model.DynamicFilterModel;
import com.tokopedia.shop.product.domain.repository.ShopProductRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class GetShopProductFilterUseCase extends UseCase<DynamicFilterModel.DataValue> {

    private ShopProductRepository shopProductRepository;

    @Inject
    public GetShopProductFilterUseCase(ShopProductRepository shopProductRepository) {
        this.shopProductRepository = shopProductRepository;
    }

    @Override
    public Observable<DynamicFilterModel.DataValue> createObservable(RequestParams requestParams) {
        return shopProductRepository.getShopProductFilter();
    }
}
