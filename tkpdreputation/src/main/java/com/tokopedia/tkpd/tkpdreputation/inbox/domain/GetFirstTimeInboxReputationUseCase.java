package com.tokopedia.tkpd.tkpdreputation.inbox.domain;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;

import rx.Observable;

/**
 * @author by nisie on 8/14/17.
 */

public class GetFirstTimeInboxReputationUseCase extends UseCase<InboxReputationDomain> {

    private static final String PARAM_PER_PAGE = "per_page";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_ROLE = "role";
    private static final String PARAM_KEYWORD = "keyword";

    private static final int DEFAULT_PER_PAGE = 10;
    private static final String PARAM_TIME_FILTER = "time_filter";
    private static final String PARAM_STATUS = "status";

    private static final int STATUS_UNASSESSED_REPUTATION = 1;
    private static final int STATUS_UPDATED_REPUTATION = 2;
    private static final int ROLE_BUYER = 1;
    private static final int ROLE_SELLER = 2;
    private static final int STATUS_OTHER = 3;

    private ReputationRepository reputationRepository;

    public GetFirstTimeInboxReputationUseCase(ThreadExecutor threadExecutor,
                                              PostExecutionThread postExecutionThread,
                                              ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<InboxReputationDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.getInboxReputationFromCloud(requestParams);
    }

    public static RequestParams getFirstTimeParam(int tab) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE);
        params.putInt(PARAM_PAGE, 1);
        params.putInt(PARAM_ROLE, getRole(tab));
        params.putInt(PARAM_TIME_FILTER, 1);
        params.putInt(PARAM_STATUS, getStatus(tab));
        return params;
    }

    public static RequestParams getParam(int page, String keyword, int timeFilter, int tab) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE);
        params.putInt(PARAM_PAGE, page);
        params.putInt(PARAM_ROLE, getRole(tab));
        if (TextUtils.isEmpty(keyword))
            params.putString(PARAM_KEYWORD, keyword);
        params.putInt(PARAM_TIME_FILTER, timeFilter);
        params.putInt(PARAM_STATUS, getStatus(tab));
        return params;
    }

    private static int getStatus(int tab) {
        switch (tab) {
            case InboxReputationActivity.TAB_WAITING_REVIEW:
                return STATUS_UNASSESSED_REPUTATION;
            case InboxReputationActivity.TAB_MY_REVIEW:
                return STATUS_UPDATED_REPUTATION;
            case InboxReputationActivity.TAB_BUYER_REVIEW:
            default:
                return STATUS_OTHER;
        }
    }

    private static int getRole(int tab) {
        switch (tab) {
            case InboxReputationActivity.TAB_WAITING_REVIEW:
                return ROLE_BUYER;
            case InboxReputationActivity.TAB_MY_REVIEW:
                return ROLE_BUYER;
            case InboxReputationActivity.TAB_BUYER_REVIEW:
                return ROLE_SELLER;
            default:
                return ROLE_BUYER;
        }
    }
}
