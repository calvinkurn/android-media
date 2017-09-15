package com.tokopedia.seller.shop.setting.data.datasource;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.shop.setting.data.mapper.UpdateShopScheduleMapperSource;
import com.tokopedia.seller.shop.setting.data.datasource.cloud.UpdateShopScheduleDataSourceCloud;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/15/17.
 */

public class UpdateShopScheduleDataSource {

    private final Context context;
    private final UpdateShopScheduleDataSourceCloud updateShopScheduleDataSourceCloud;

    @Inject
    public UpdateShopScheduleDataSource(@ApplicationContext Context context, UpdateShopScheduleDataSourceCloud updateShopScheduleDataSourceCloud) {
        this.context = context;
        this.updateShopScheduleDataSourceCloud = updateShopScheduleDataSourceCloud;
    }

    public Observable<Boolean> updateShopSchedule(TKPDMapParam<String, String> requestParams) {
        return updateShopScheduleDataSourceCloud.updateShopSchedule(AuthUtil.generateParamsNetwork(context, requestParams))
                .map(new UpdateShopScheduleMapperSource());
    }
}
