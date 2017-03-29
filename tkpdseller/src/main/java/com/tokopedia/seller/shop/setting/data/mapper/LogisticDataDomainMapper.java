package com.tokopedia.seller.shop.setting.data.mapper;

import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.OpenShopLogisticServiceModel;
import com.tokopedia.seller.shop.setting.domain.model.LogisticAvailableDomainModel;

import rx.functions.Func1;

/**
 * Created by sebastianuskh on 3/27/17.
 */

public class LogisticDataDomainMapper implements Func1<OpenShopLogisticServiceModel, LogisticAvailableDomainModel> {
    @Override
    public LogisticAvailableDomainModel call(OpenShopLogisticServiceModel openShopLogisticServiceModel) {
        return null;
    }
}
