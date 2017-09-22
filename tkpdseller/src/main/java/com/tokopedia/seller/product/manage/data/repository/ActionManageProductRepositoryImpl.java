package com.tokopedia.seller.product.manage.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.manage.data.source.ActionManageProductDataSource;
import com.tokopedia.seller.product.manage.domain.ActionManageProductRepository;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ActionManageProductRepositoryImpl implements ActionManageProductRepository {

    private final ActionManageProductDataSource actionManageProductDataSource;

    public ActionManageProductRepositoryImpl(ActionManageProductDataSource actionManageProductDataSource) {
        this.actionManageProductDataSource = actionManageProductDataSource;
    }

    @Override
    public Observable<Boolean> editPrice(TKPDMapParam<String, String> params) {
        return actionManageProductDataSource.editPrice(params);
    }

    @Override
    public Observable<Boolean> deleteProduct(TKPDMapParam<String, String> params) {
        return actionManageProductDataSource.deleteProduct(params);
    }
}
