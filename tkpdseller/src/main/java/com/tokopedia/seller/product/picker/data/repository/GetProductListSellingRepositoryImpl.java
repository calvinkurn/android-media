package com.tokopedia.seller.product.picker.data.repository;

import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.picker.data.source.GetProductListSellingDataSource;
import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;
import com.tokopedia.seller.product.picker.domain.GetProductListSellingRepository;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/4/17.
 */

public class GetProductListSellingRepositoryImpl implements GetProductListSellingRepository {

    private final GetProductListSellingDataSource getProductListSellingDataSource;

    public GetProductListSellingRepositoryImpl(GetProductListSellingDataSource getProductListSellingDataSource) {
        this.getProductListSellingDataSource = getProductListSellingDataSource;
    }

    @Override
    public Observable<ProductListSellerModel> getProductListSeller(TKPDMapParam<String, String> parameters) {
        return getProductListSellingDataSource.getProductListSeller(parameters)
                .map(new GetData<ProductListSellerModel>());
    }
}
