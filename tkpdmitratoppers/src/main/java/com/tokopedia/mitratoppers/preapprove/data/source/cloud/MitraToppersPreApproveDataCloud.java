package com.tokopedia.mitratoppers.preapprove.data.source.cloud;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.mapper.DataResponseMapper;
import com.tokopedia.mitratoppers.preapprove.data.source.cloud.api.MitraToppersApi;
import com.tokopedia.mitratoppers.common.di.MitraToppersQualifier;
import com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove.ResponsePreApprove;

import java.lang.reflect.Type;

import javax.inject.Inject;

import rx.Observable;

public class MitraToppersPreApproveDataCloud {
    private final MitraToppersApi api;
    private final UserSession userSession;

    @Inject
    public MitraToppersPreApproveDataCloud(MitraToppersApi api,
                                           UserSession userSession) {
        this.api = api;
        this.userSession = userSession;
    }

//    public Observable<ResponsePreApprove> getPreApproveBalance() {
//        return api.preApproveBalance(userSession.getShopId())
//                .map(new DataResponseMapper<ResponsePreApprove>());
//    }

    public Observable<ResponsePreApprove> getPreApproveBalance() {
        Gson g = new Gson();
        Type dataResponseType = new TypeToken<DataResponse<ResponsePreApprove>>() {}.getType();
        DataResponse<ResponsePreApprove> dataResponse = g.fromJson(loadJSONFromAsset(), dataResponseType);
        return Observable.just(dataResponse.getData());
    }

    public String loadJSONFromAsset() {
        return "{\"data\":{\"preapp\":{\"valid\":\"2017-11-26\",\"3m\":{\"amount\":20000000,\"amount_idr\":\"Rp 20.000.000\",\"rate\":1.59},\"6m\":{\"amount\":20000000,\"amount_idr\":\"Rp 20.000.000\",\"rate\":1.59},\"12m\":{\"amount\":20000000,\"amount_idr\":\"Rp 20.000.000\",\"rate\":1.59}},\"url\":\"https://staging.tokopedia.com/mitra-toppers/preapproved?ml=0&il=0&i3_amount=0&i6_amount=0&i12_amount=0&i3_rate=1.59&i6_rate=1.59&i12_rate=1.59\"},\"code\":200,\"header\":{\"process_time\":0.02195738,\"messages\":[],\"reason\":\"\",\"error_code\":\"\"}}";
    }
}
