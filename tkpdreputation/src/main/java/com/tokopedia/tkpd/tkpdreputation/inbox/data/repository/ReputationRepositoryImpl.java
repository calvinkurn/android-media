package com.tokopedia.tkpd.tkpdreputation.inbox.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.factory.ReputationFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewSubmitDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendSmileyReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SkipReviewDomain;

import rx.Observable;

/**
 * @author by nisie on 8/14/17.
 */

public class ReputationRepositoryImpl implements ReputationRepository {

    ReputationFactory reputationFactory;

    public ReputationRepositoryImpl(ReputationFactory reputationFactory) {
        this.reputationFactory = reputationFactory;
    }

    @Override
    public Observable<InboxReputationDomain> getInboxReputationFromCloud(RequestParams requestParams) {
        return reputationFactory
                .createCloudInboxReputationDataSource()
                .getInboxReputation(requestParams);
    }

    @Override
    public Observable<InboxReputationDomain> getInboxReputationFromLocal() {
        return null;
    }

    @Override
    public Observable<InboxReputationDetailDomain> getInboxReputationDetailFromCloud(RequestParams requestParams) {
        return reputationFactory
                .createCloudInboxReputationDetailDataSource()
                .getInboxReputationDetail(requestParams);
    }

    @Override
    public Observable<SendSmileyReputationDomain> sendSmiley(RequestParams requestParams) {
        return reputationFactory
                .createCloudSendSmileyReputationDataSource()
                .sendSmiley(requestParams);
    }

    @Override
    public Observable<SendReviewValidateDomain> sendReviewValidation(RequestParams requestParams) {
        return reputationFactory
                .createCloudSendReviewValidationDataSource()
                .sendReviewValidation(requestParams);
    }

    @Override
    public Observable<SendReviewSubmitDomain> sendReviewSubmit(RequestParams requestParams) {
        return reputationFactory
                .createCloudSendReviewSubmitDataSource()
                .sendReviewSubmit(requestParams);
    }

    @Override
    public Observable<SkipReviewDomain> skipReview(RequestParams requestParams) {
        return reputationFactory
                .createCloudSkipReviewDataSource()
                .skipReview(requestParams);
    }

    @Override
    public Observable<SendReviewValidateDomain> editReviewValidation(RequestParams requestParams) {
        return reputationFactory
                .createCloudSendReviewValidationDataSource()
                .editReviewValidation(requestParams);
    }

    @Override
    public Observable<SendReviewSubmitDomain> editReviewSubmit(RequestParams requestParams) {
        return reputationFactory
                .createCloudSendReviewSubmitDataSource()
                .editReviewSubmit(requestParams);
    }
}
