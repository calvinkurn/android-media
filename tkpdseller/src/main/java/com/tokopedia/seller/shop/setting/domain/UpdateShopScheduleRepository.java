package com.tokopedia.seller.shop.setting.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/15/17.
 */

public interface UpdateShopScheduleRepository {
    Observable<Boolean> updateShopSchedule(TKPDMapParam<String, String> requestParams);
}
