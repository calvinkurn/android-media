package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;

import rx.Observable;

/**
 * @author by nisie on 8/18/17.
 */

public class GetInboxReputationUseCase extends GetFirstTimeInboxReputationUseCase {

    private static final String PARAM_KEYWORD = "keyword";

    public GetInboxReputationUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread, reputationRepository);
    }

    @Override
    public Observable<InboxReputationDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.getInboxReputationFromCloud(requestParams);
    }

    public static RequestParams getParam(int page, String keyword, String timeFilter,
                                         String readStatusFilter, int tab) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE);
        params.putInt(PARAM_PAGE, page);
        params.putInt(PARAM_ROLE, getRole(tab));
        if (!TextUtils.isEmpty(keyword))
            params.putString(PARAM_KEYWORD, keyword);
        params.putString(PARAM_TIME_FILTER, !TextUtils.isEmpty(timeFilter) ? timeFilter :
                DEFAULT_TIME_FILTER);
        if (!TextUtils.isEmpty(readStatusFilter))
            params.putString(PARAM_READ_STATUS, !TextUtils.isEmpty(readStatusFilter) ?
                    readStatusFilter : DEFAULT_READ_STATUS);
        params.putInt(PARAM_STATUS, getStatus(tab));
        return params;
    }
}
