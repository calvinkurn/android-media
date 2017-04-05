package com.tokopedia.seller.product.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.product.data.source.cloud.api.MyEtalaseApi;
import com.tokopedia.seller.product.data.source.cloud.model.myetalase.MyEtalaseListServiceModel;
import com.tokopedia.seller.shopscore.data.common.GetData;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class MyEtalaseCloud {
    public static final String SHOP_ID = "shop_id";
    private final MyEtalaseApi api;
    private final Context context;

    @Inject
    public MyEtalaseCloud(MyEtalaseApi api, @ActivityContext Context context) {
        this.api = api;
        this.context = context;
    }

    public Observable<MyEtalaseListServiceModel> fetchMyEtalaseList() {
        TKPDMapParam<String, String> params = generateFetchMyEtalaseParam();
        return api.fetchMyEtalase(AuthUtil.generateParamsNetwork(context, params))
                .map(new GetData<MyEtalaseListServiceModel>());
    }

    private TKPDMapParam<String, String> generateFetchMyEtalaseParam() {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(SHOP_ID, new SessionHandler(context).getShopID());
        return param;
    }
}
