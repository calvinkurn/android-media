package com.tokopedia.mitratoppers.common.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.mapper.DataResponseMapper;
import com.tokopedia.mitratoppers.common.data.source.cloud.api.MitraToppersApi;
import com.tokopedia.mitratoppers.common.di.MitraToppersQualifier;
import com.tokopedia.mitratoppers.dashboard.data.model.response.preapprove.ResponsePreApprove;

import javax.inject.Inject;

import rx.Observable;

public class MitraToppersDataCloud {
    private final MitraToppersApi api;
    private final UserSession userSession;

    @Inject
    public MitraToppersDataCloud(@MitraToppersQualifier MitraToppersApi api,
                                 UserSession userSession) {
        this.api = api;
        this.userSession = userSession;
    }

    public Observable<ResponsePreApprove> getPreApproveBalance() {
        return api.preApproveBalance(userSession.getShopId())
                .map(new DataResponseMapper<ResponsePreApprove>());
    }

}
