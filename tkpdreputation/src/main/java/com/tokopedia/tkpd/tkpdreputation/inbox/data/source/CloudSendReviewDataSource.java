package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewValidateMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendReviewValidateDomain;

import rx.Observable;

/**
 * @author by nisie on 8/31/17.
 */

public class CloudSendReviewDataSource {
    private final ReputationService reputationService;
    private final SendReviewValidateMapper sendReviewValidateMapper;

    public CloudSendReviewDataSource(ReputationService reputationService,
                                     SendReviewValidateMapper sendReviewValidateMapper) {
        this.reputationService = reputationService;
        this.sendReviewValidateMapper = sendReviewValidateMapper;

    }

    public Observable<SendReviewValidateDomain> sendReviewValidation(RequestParams requestParams) {
        return reputationService
                .getApi()
                .sendReviewValidate(requestParams.getParameters())
                .map(sendReviewValidateMapper);
    }
}
