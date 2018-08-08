package com.tokopedia.seller.shop.setting.data.datasource.cloud;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.seller.shop.setting.data.model.DataResponseShopSchedule;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by zulfikarrahman on 9/15/17.
 */

public class UpdateShopScheduleDataSourceCloud {

    private final ShopScheduleApi shopScheduleApi;

    @Inject
    public UpdateShopScheduleDataSourceCloud(ShopScheduleApi shopScheduleApi) {
        this.shopScheduleApi = shopScheduleApi;
    }

    public Observable<Response<DataResponse<DataResponseShopSchedule>>> updateShopSchedule(TKPDMapParam<String, String> params) {
        return shopScheduleApi.updateShopClose(params);
    }
}
