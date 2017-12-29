package com.tokopedia.seller.shop.setting.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.OpenShopDistrictServiceModel;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.shop.open.data.model.OpenShopCouriersModel;
import com.tokopedia.seller.shop.open.data.source.cloud.api.OpenShopApi;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 3/20/17.
 */

public class DistrictLogisticDataCloud {
    public static final String DISTRICT = "district";
    public static final String YES = "1";
    public static final String NO = "0";
    public static final String COURIER = "courier";
    public static final String DISTRICT_ID = "district_id";
    private final OpenShopApi api;
    private final Context context;

    @Inject
    public DistrictLogisticDataCloud(@ApplicationContext Context context, OpenShopApi api) {
        this.api = api;
        this.context = context;
    }

    public Observable<OpenShopDistrictServiceModel> fetchDistrictData() {
        return api
                .fetchDistrictData(
                        AuthUtil.generateParamsNetwork(
                                context, getFetchDistrictDataParams()
                        )
                )
                .map(new GetData<OpenShopDistrictServiceModel>());
    }

    private TKPDMapParam<String, String> getFetchDistrictDataParams() {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(DISTRICT, YES);
        param.put(COURIER, NO);
        return param;
    }


    public Observable<OpenShopCouriersModel> getAvailableCouriers(int districtCode) {
        return api.getLogisticAvailable(AuthUtil.generateParamsNetwork(context, getLogisticAvailableDataparams(districtCode)))
                .map(new SimpleDataResponseMapper<OpenShopCouriersModel>());
    }

    public TKPDMapParam<String, String> getLogisticAvailableDataparams(int districtCode) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(COURIER, YES);
        param.put(DISTRICT, NO);
        param.put(DISTRICT_ID, String.valueOf(districtCode));
        return param;
    }
}
