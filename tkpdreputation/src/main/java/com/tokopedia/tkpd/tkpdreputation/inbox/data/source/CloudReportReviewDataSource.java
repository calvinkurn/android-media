package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.ReportReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ReportReviewDomain;

import rx.Observable;

/**
 * @author by nisie on 9/13/17.
 */

public class CloudReportReviewDataSource {
    private final ReputationService reputationService;
    private final ReportReviewMapper reportReviewMapper;

    public CloudReportReviewDataSource(ReputationService reputationService,
                                       ReportReviewMapper reportReviewMapper) {
        this.reputationService = reputationService;
        this.reportReviewMapper = reportReviewMapper;
    }


    public Observable<ReportReviewDomain> reportReview(RequestParams requestParams) {
        return reputationService.getApi()
                .reportReview(requestParams.getParameters())
                .map(reportReviewMapper);
    }
}
