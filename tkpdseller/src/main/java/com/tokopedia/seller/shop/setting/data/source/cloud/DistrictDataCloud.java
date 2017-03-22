package com.tokopedia.seller.shop.setting.data.source.cloud;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.common.util.GetData;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.network.apiservices.shop.apis.MyShopApi;
import com.tokopedia.core.network.apiservices.shop.apis.model.openshopdistrict.OpenShopDistrictServiceModel;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.shop.setting.data.source.DistrictDataSource;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class DistrictDataCloud {
    public static final String DISTRICT = "district";
    public static final String YES = "1";
    private final MyShopApi api;
    private final Context context;

    @Inject
    public DistrictDataCloud(@ActivityContext Context context, MyShopApi api) {
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
        return param;
    }


}
