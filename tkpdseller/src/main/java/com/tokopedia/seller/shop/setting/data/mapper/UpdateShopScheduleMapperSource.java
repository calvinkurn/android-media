package com.tokopedia.seller.shop.setting.data.mapper;

import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.seller.shop.setting.data.model.DataResponseShopSchedule;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 9/15/17.
 */

public class UpdateShopScheduleMapperSource implements Func1<Response<DataResponse<DataResponseShopSchedule>>, Boolean> {

    @Override
    public Boolean call(Response<DataResponse<DataResponseShopSchedule>> dataResponse) {
        if(dataResponse.isSuccessful() && dataResponse.body() != null
                && dataResponse.body().getData() != null && dataResponse.body().getData().getIs_success() == 1){
            return true;
        }else {
            return false;
        }
    }
}
