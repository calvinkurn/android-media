package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendSmileyReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendSmileyReputationDomain;

import rx.Observable;

/**
 * @author by nisie on 8/31/17.
 */

public class CloudSendSmileyReputationDataSource {
    private final ReputationService reputationService;
    private final SendSmileyReputationMapper sendSmileyReputationMapper;

    public CloudSendSmileyReputationDataSource(ReputationService reputationService,
                                               SendSmileyReputationMapper sendSmileyReputationMapper) {
        this.reputationService = reputationService;
        this.sendSmileyReputationMapper = sendSmileyReputationMapper;

    }

    public Observable<SendSmileyReputationDomain> sendSmiley(RequestParams requestParams) {
        return reputationService
                .getApi()
                .sendSmiley(requestParams.getParameters())
                .map(sendSmileyReputationMapper);
    }
}
