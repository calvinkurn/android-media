package com.tokopedia.seller.product.manage.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/20/17.
 */

public interface ActionProductManageRepository {
    Observable<Boolean> editPrice(TKPDMapParam<String, String> params);

    Observable<Boolean> deleteProduct(TKPDMapParam<String, String> params);
}
