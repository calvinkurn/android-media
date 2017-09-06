package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewSubmitMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewSubmitDomain;

import rx.Observable;

/**
 * @author by nisie on 9/5/17.
 */

public class CloudSendReviewSubmitDataSource {

    private final ReputationService reputationService;
    private final SendReviewSubmitMapper sendReviewSubmitMapper;

    public CloudSendReviewSubmitDataSource(ReputationService reputationService,
                                           SendReviewSubmitMapper sendReviewSubmitMapper) {
    this.reputationService = reputationService;
        this.sendReviewSubmitMapper = sendReviewSubmitMapper;
    }

    public Observable<SendReviewSubmitDomain> sendReviewSubmit(RequestParams requestParams) {
        return reputationService.getApi()
                .sendReviewSubmit(requestParams.getParameters())
                .map(sendReviewSubmitMapper);
    }
}
