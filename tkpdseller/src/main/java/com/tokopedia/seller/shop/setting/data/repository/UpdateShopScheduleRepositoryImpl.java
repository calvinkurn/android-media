package com.tokopedia.seller.shop.setting.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.shop.setting.data.datasource.UpdateShopScheduleDataSource;
import com.tokopedia.seller.shop.setting.domain.UpdateShopScheduleRepository;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/15/17.
 */

public class UpdateShopScheduleRepositoryImpl implements UpdateShopScheduleRepository {

    private UpdateShopScheduleDataSource updateShopScheduleDataSource;

    public UpdateShopScheduleRepositoryImpl(UpdateShopScheduleDataSource updateShopScheduleDataSource) {
        this.updateShopScheduleDataSource = updateShopScheduleDataSource;
    }

    @Override
    public Observable<Boolean> updateShopSchedule(TKPDMapParam<String, String> requestParams) {
        return updateShopScheduleDataSource.updateShopSchedule(requestParams);
    }
}
