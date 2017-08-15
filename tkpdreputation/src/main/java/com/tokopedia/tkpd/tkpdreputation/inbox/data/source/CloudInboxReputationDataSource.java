package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;

import rx.Observable;

/**
 * @author by nisie on 8/14/17.
 */

public class CloudInboxReputationDataSource {
    private final InboxReputationMapper inboxReputationMapper;
    private final GlobalCacheManager globalCacheManager;
    private final ReputationService reputationService;

    public CloudInboxReputationDataSource(ReputationService reputationService,
                                          InboxReputationMapper inboxReputationMapper,
                                          GlobalCacheManager globalCacheManager) {

        this.reputationService = reputationService;
        this.inboxReputationMapper = inboxReputationMapper;
        this.globalCacheManager = globalCacheManager;
    }


    public Observable<InboxReputationDomain> getInboxReputation(RequestParams requestParams) {
        return reputationService.getApi().getInbox(
                AuthUtil.generateParamsNetwork2(
                        MainApplication.getAppContext(),
                        requestParams.getParameters()))
                .map(inboxReputationMapper);
    }
}
