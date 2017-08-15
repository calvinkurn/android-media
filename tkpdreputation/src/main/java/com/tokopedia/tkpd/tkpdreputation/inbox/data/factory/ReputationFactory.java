package com.tokopedia.tkpd.tkpdreputation.inbox.data.factory;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.user.InboxReviewService;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudInboxReputationDataSource;

/**
 * @author by nisie on 8/14/17.
 */

public class ReputationFactory {

    private final ReputationService reputationService;
    private final InboxReputationMapper inboxReputationMapper;
    private final GlobalCacheManager globalCacheManager;

    public ReputationFactory(ReputationService reputationService,
                             InboxReputationMapper inboxReputationMapper,
                             GlobalCacheManager globalCacheManager) {
        this.reputationService = reputationService;
        this.inboxReputationMapper = inboxReputationMapper;
        this.globalCacheManager = globalCacheManager;
    }

    public CloudInboxReputationDataSource createCloudInboxReputationDataSource() {
        return new CloudInboxReputationDataSource(reputationService, inboxReputationMapper, globalCacheManager);
    }
}
