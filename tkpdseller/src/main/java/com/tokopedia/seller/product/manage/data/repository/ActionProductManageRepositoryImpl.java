package com.tokopedia.seller.product.manage.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.manage.data.source.ActionProductManageDataSource;
import com.tokopedia.seller.product.manage.domain.ActionProductManageRepository;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ActionProductManageRepositoryImpl implements ActionProductManageRepository {

    private final ActionProductManageDataSource actionProductManageDataSource;

    public ActionProductManageRepositoryImpl(ActionProductManageDataSource actionProductManageDataSource) {
        this.actionProductManageDataSource = actionProductManageDataSource;
    }

    @Override
    public Observable<Boolean> editPrice(TKPDMapParam<String, String> params) {
        return actionProductManageDataSource.editPrice(params);
    }

    @Override
    public Observable<Boolean> deleteProduct(TKPDMapParam<String, String> params) {
        return actionProductManageDataSource.deleteProduct(params);
    }
}
