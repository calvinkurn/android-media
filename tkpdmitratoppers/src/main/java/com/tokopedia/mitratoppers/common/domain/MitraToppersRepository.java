package com.tokopedia.mitratoppers.common.domain;

import com.tokopedia.mitratoppers.preapprove.data.source.cloud.MitraToppersPreApproveDataCloud;
import com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove.ResponsePreApprove;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class MitraToppersRepository {
    private final MitraToppersPreApproveDataCloud mitraToppersPreApproveDataCloud;

    @Inject
    public MitraToppersRepository(MitraToppersPreApproveDataCloud mitraToppersPreApproveDataCloud) {
        this.mitraToppersPreApproveDataCloud = mitraToppersPreApproveDataCloud;
    }

    public Observable<ResponsePreApprove> getPreApproveBalance() {
        return mitraToppersPreApproveDataCloud.getPreApproveBalance();
    }
}
