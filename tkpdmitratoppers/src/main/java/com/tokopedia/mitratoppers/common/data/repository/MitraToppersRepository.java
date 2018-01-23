package com.tokopedia.mitratoppers.common.data.repository;

import com.tokopedia.mitratoppers.common.data.source.cloud.MitraToppersDataCloud;
import com.tokopedia.mitratoppers.dashboard.data.model.response.preapprove.ResponsePreApprove;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class MitraToppersRepository {
    MitraToppersDataCloud mitraToppersDataCloud;

    @Inject
    public MitraToppersRepository(MitraToppersDataCloud mitraToppersDataCloud) {
        this.mitraToppersDataCloud = mitraToppersDataCloud;
    }

    public Observable<ResponsePreApprove> getPreApproveBalance() {
        return mitraToppersDataCloud.getPreApproveBalance();
    }
}
