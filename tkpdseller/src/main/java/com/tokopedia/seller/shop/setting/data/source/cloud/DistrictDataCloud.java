package com.tokopedia.seller.shop.setting.data.source.cloud;

import com.tokopedia.core.network.apiservices.shop.apis.MyShopApi;
import com.tokopedia.core.network.apiservices.shop.apis.model.OpenShopDistrictModel;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class DistrictDataCloud {
    public static final String DISTRICT = "district";
    public static final String YES = "1";
    private final MyShopApi api;

    public DistrictDataCloud(MyShopApi api) {
        this.api = api;
    }

    public Observable<OpenShopDistrictModel> fetchDistrictData() {
        return api.fetchDistrictData(getFetchDistrictDataParams());
    }

    private Map<String, String> getFetchDistrictDataParams() {
        Map<String, String> param = new TKPDMapParam<>();
        param.put(DISTRICT, YES);
        return param;
    }
}
