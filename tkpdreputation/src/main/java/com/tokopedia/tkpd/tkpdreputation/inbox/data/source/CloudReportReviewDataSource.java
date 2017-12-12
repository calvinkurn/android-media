package com.tokopedia.tkpd.tkpdreputation.inbox.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.ReportReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.domain.model.ReportReviewDomain;

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
                .reportReview(AuthUtil.generateParamsNetwork2(
                        MainApplication.getAppContext(),
                        requestParams.getParameters()))
                .map(reportReviewMapper);
    }
}
