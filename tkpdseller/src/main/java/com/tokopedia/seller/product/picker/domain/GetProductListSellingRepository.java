package com.tokopedia.seller.product.picker.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/4/17.
 */

public interface GetProductListSellingRepository {
    Observable<ProductListSellerModel> getProductListSeller(TKPDMapParam<String, String> parameters);
}
