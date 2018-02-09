package com.tokopedia.mitratoppers.preapprove.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.mapper.DataResponseMapper;
import com.tokopedia.mitratoppers.preapprove.data.source.cloud.api.MitraToppersApi;
import com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove.ResponsePreApprove;

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

    public Observable<ResponsePreApprove> getPreApproveBalance() {
        return api.preApproveBalance(userSession.getShopId())
                .map(new DataResponseMapper<ResponsePreApprove>());
    }

}
