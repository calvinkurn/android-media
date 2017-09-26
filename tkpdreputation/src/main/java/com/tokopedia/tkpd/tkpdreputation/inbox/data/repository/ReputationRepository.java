package com.tokopedia.tkpd.tkpdreputation.inbox.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ReportReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewSubmitDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendSmileyReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SkipReviewDomain;

import rx.Observable;

/**
 * @author by nisie on 8/14/17.
 */

public interface ReputationRepository {

    Observable<InboxReputationDomain> getInboxReputationFromCloud(RequestParams requestParams);

    Observable<InboxReputationDomain> getInboxReputationFromLocal(RequestParams requestParams);

    Observable<ReviewDomain> getReviewFromCloud(RequestParams requestParams);

    Observable<SendSmileyReputationDomain> sendSmiley(RequestParams requestParams);

    Observable<SendReviewValidateDomain> sendReviewValidation(RequestParams requestParams);

    Observable<SendReviewSubmitDomain> sendReviewSubmit(RequestParams requestParams);

    Observable<SkipReviewDomain> skipReview(RequestParams requestParams);

    Observable<SendReviewValidateDomain> editReviewValidation(RequestParams requestParams);

    Observable<SendReviewSubmitDomain> editReviewSubmit(RequestParams requestParams);

    Observable<ReportReviewDomain> reportReview(RequestParams requestParams);
}
