package com.tokopedia.tkpd.tkpdreputation.inbox.data.factory;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationDetailMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewSubmitMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewValidateMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendSmileyReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SkipReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudInboxReputationDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudInboxReputationDetailDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudSendReviewDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudSendReviewSubmitDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudSendSmileyReputationDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudSkipReviewDataSource;

/**
 * @author by nisie on 8/14/17.
 */

public class ReputationFactory {

    private final ReputationService reputationService;
    private final InboxReputationMapper inboxReputationMapper;
    private final GlobalCacheManager globalCacheManager;
    private final InboxReputationDetailMapper inboxReputationDetailMapper;
    private final SendSmileyReputationMapper sendSmileyReputationMapper;
    private final SendReviewValidateMapper sendReviewValidateMapper;
    private final SendReviewSubmitMapper sendReviewSubmitMapper;
    private final SkipReviewMapper skipReviewMapper;


    public ReputationFactory(ReputationService reputationService,
                             InboxReputationMapper inboxReputationMapper,
                             InboxReputationDetailMapper inboxReputationDetailMapper,
                             SendSmileyReputationMapper sendSmileyReputationMapper,
                             SendReviewValidateMapper sendReviewValidateMapper,
                             SendReviewSubmitMapper sendReviewSubmitMapper,
                             SkipReviewMapper skipReviewMapper,
                             GlobalCacheManager globalCacheManager) {
        this.reputationService = reputationService;
        this.globalCacheManager = globalCacheManager;
        this.inboxReputationMapper = inboxReputationMapper;
        this.inboxReputationDetailMapper = inboxReputationDetailMapper;
        this.sendSmileyReputationMapper = sendSmileyReputationMapper;
        this.sendReviewValidateMapper = sendReviewValidateMapper;
        this.sendReviewSubmitMapper = sendReviewSubmitMapper;
        this.skipReviewMapper = skipReviewMapper;
    }

    public CloudInboxReputationDataSource createCloudInboxReputationDataSource() {
        return new CloudInboxReputationDataSource(reputationService, inboxReputationMapper, globalCacheManager);
    }

    public CloudInboxReputationDetailDataSource createCloudInboxReputationDetailDataSource() {
        return new CloudInboxReputationDetailDataSource(reputationService,
                inboxReputationDetailMapper);
    }

    public CloudSendSmileyReputationDataSource createCloudSendSmileyReputationDataSource() {
        return new CloudSendSmileyReputationDataSource(reputationService,
                sendSmileyReputationMapper);
    }

    public CloudSendReviewDataSource createCloudSendReviewValidationDataSource() {
        return new CloudSendReviewDataSource(reputationService,
                sendReviewValidateMapper);
    }

    public CloudSendReviewSubmitDataSource createCloudSendReviewSubmitDataSource() {
        return new CloudSendReviewSubmitDataSource(reputationService,
                sendReviewSubmitMapper);
    }

    public CloudSkipReviewDataSource createCloudSkipReviewDataSource() {
        return new CloudSkipReviewDataSource(reputationService,
                skipReviewMapper);
    }
}
