package com.tokopedia.transaction.bcaoneklik.domain;

import com.google.gson.Gson;
import com.tokopedia.core.manage.people.bank.model.BcaOneClickData;
import com.tokopedia.core.network.apiservices.payment.BcaOneClickService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickRegisterData;
import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickSuccessRegisterData;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 7/25/17. Tokopedia
 */

public class BcaOneClickFormRepository implements IBcaOneClickFormRepository{

    private BcaOneClickService bcaOneClickService;

    public BcaOneClickFormRepository(BcaOneClickService bcaOneClickService) {
        this.bcaOneClickService = bcaOneClickService;
    }

    @Override
    public Observable<BcaOneClickData> getBcaOneClickAccessToken(TKPDMapParam<String, String> bcaOneClickParam) {
        return bcaOneClickService.getApi().getBcaOneClickAccessToken(bcaOneClickParam)
                .map(new Func1<Response<String>, BcaOneClickData>() {
                    @Override
                    public BcaOneClickData call(Response<String> stringResponse) {
                        return new Gson().fromJson(stringResponse.body(), BcaOneClickData.class);
                    }
                });
    }

    @Override
    public Observable<BcaOneClickSuccessRegisterData> registerBcaOneClickData(TKPDMapParam<String, String> bcaOneClickRegisterParam) {

        return bcaOneClickService.getApi().registerBcaOneClickUserData(bcaOneClickRegisterParam)
                .map(new Func1<Response<String>, BcaOneClickSuccessRegisterData>() {
            @Override
            public BcaOneClickSuccessRegisterData call(Response<String> stringResponse) {
                return new Gson().fromJson(stringResponse.body(),
                        BcaOneClickSuccessRegisterData.class);
            }
        });
    }


}
