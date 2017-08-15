package com.tokopedia.tkpd.tkpdreputation.inbox.data;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;

import rx.Observable;

/**
 * @author by nisie on 8/14/17.
 */

public interface ReputationRepository {

    Observable<InboxReputationDomain> getInboxReputationFromCloud(RequestParams requestParams);

    Observable<InboxReputationDomain> getInboxReputationFromLocal();

}
