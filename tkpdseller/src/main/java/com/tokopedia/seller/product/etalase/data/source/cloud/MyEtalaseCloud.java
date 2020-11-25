package com.tokopedia.seller.product.etalase.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.manageitem.data.cloud.model.etalase.MyEtalaseListServiceModel;
import com.tokopedia.seller.product.common.utils.GetData;
import com.tokopedia.seller.product.etalase.data.source.cloud.api.MyEtalaseApi;
import com.tokopedia.seller.product.etalase.data.source.cloud.model.AddEtalaseServiceModel;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class MyEtalaseCloud {
    public static final String SHOP_ID = "shop_id";
    public static final String ETALASE_NAME = "etalase_name";
    public static final String PAGE = "page";
    private final MyEtalaseApi api;
    private final Context context;

    @Inject
    public MyEtalaseCloud(MyEtalaseApi api, @ApplicationContext Context context) {
        this.api = api;
        this.context = context;
    }

    public Observable<MyEtalaseListServiceModel> fetchMyEtalaseList(int page) {
        TKPDMapParam<String, String> params = generateFetchMyEtalaseParam(page);
        return api.fetchMyEtalase(AuthUtil.generateParamsNetwork(context, params))
                .map(new GetData<MyEtalaseListServiceModel>());
    }

    private TKPDMapParam<String, String> generateFetchMyEtalaseParam(int page) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        UserSessionInterface userSession = new UserSession(context);
        param.put(SHOP_ID, userSession.getShopId());
        param.put(PAGE, String.valueOf(page));
        return param;
    }

    public Observable<AddEtalaseServiceModel> addNewEtalase(String newEtalaseName) {
        TKPDMapParam<String, String> param = geenerateAddNewEtalaseparam(newEtalaseName);
        return api.addNewEtalase(AuthUtil.generateParamsNetwork(context, param))
                .map(new GetData<AddEtalaseServiceModel>());
    }

    private TKPDMapParam<String, String> geenerateAddNewEtalaseparam(String newEtalaseName) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(ETALASE_NAME, newEtalaseName);
        return param;
    }
}
