package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ReviewItemDomain;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 8/19/17.
 */

public class GetInboxReputationDetailUseCase extends UseCase<InboxReputationDetailDomain> {

    private final GetLikeDislikeReviewUseCase getLikeDislikeReviewUseCase;
    private final GetInboxReputationUseCase getInboxReputationUseCase;
    private final GetReviewUseCase getReviewUseCase;

    public GetInboxReputationDetailUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread,
                                           GetInboxReputationUseCase getInboxReputationUseCase,
                                           GetReviewUseCase getReviewUseCase,
                                           GetLikeDislikeReviewUseCase getLikeDislikeReviewUseCase) {
        super(threadExecutor, postExecutionThread);
        this.getInboxReputationUseCase = getInboxReputationUseCase;
        this.getReviewUseCase = getReviewUseCase;
        this.getLikeDislikeReviewUseCase = getLikeDislikeReviewUseCase;
    }

    @Override
    public Observable<InboxReputationDetailDomain> createObservable(RequestParams requestParams) {
        InboxReputationDetailDomain domain = new InboxReputationDetailDomain();
        return getReputation(domain, getReputationParam(requestParams))
                .flatMap(getReview(domain, getReviewParam(requestParams)))
                .flatMap(getLikeDislike(domain, requestParams));
    }

    private Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>>
    getLikeDislike(final InboxReputationDetailDomain domain, final RequestParams requestParams) {
        return new Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>>() {
            @Override
            public Observable<InboxReputationDetailDomain> call(InboxReputationDetailDomain inboxReputationDetailDomain) {
                return getLikeDislikeReviewUseCase.createObservable(
                        GetLikeDislikeReviewUseCase.getParam(
                                getListReviewId(domain.getReviewDomain().getData()),
                                requestParams.getString(GetReviewUseCase.PARAM_USER_ID, "")
                        ))
                        .flatMap(new Func1<GetLikeDislikeReviewDomain, Observable<InboxReputationDetailDomain>>() {
                            @Override
                            public Observable<InboxReputationDetailDomain> call
                                    (GetLikeDislikeReviewDomain getLikeDislikeReviewDomain) {
                                return Observable.just(domain);
                            }
                        });
            }
        };
    }

    private String getListReviewId(List<ReviewItemDomain> data) {
        String reviewIds = "";
        for (ReviewItemDomain itemDomain : data) {
            if (data.size() > 1)
                reviewIds += itemDomain.getReviewId() + "~";
            else
                reviewIds += itemDomain.getReviewId();
        }
        return reviewIds;
    }

    private RequestParams getReputationParam(RequestParams requestParams) {
        return GetInboxReputationUseCase.getSpecificReputation(
                requestParams.getString(GetInboxReputationUseCase.PARAM_REPUTATION_ID, ""),
                requestParams.getInt(GetInboxReputationUseCase.PARAM_TAB, 0)
        );
    }

    private Observable<InboxReputationDetailDomain> getReputation(final InboxReputationDetailDomain domain, RequestParams reputationParam) {
        return getInboxReputationUseCase.createObservable(reputationParam)
                .flatMap(new Func1<InboxReputationDomain, Observable<InboxReputationDetailDomain>>() {
                    @Override
                    public Observable<InboxReputationDetailDomain> call(InboxReputationDomain inboxReputationDomain) {
                        domain.setInboxReputationDomain(inboxReputationDomain);
                        return Observable.just(domain);
                    }
                });
    }


    private RequestParams getReviewParam(RequestParams requestParams) {
        return GetReviewUseCase.getParam(
                requestParams.getString(GetReviewUseCase.PARAM_REPUTATION_ID, ""),
                requestParams.getString(GetReviewUseCase.PARAM_USER_ID, ""),
                requestParams.getInt(GetInboxReputationUseCase.PARAM_TAB, 0)
        );
    }

    private Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>> getReview(
            final InboxReputationDetailDomain domain, final RequestParams reviewParam) {
        return new Func1<InboxReputationDetailDomain, Observable<InboxReputationDetailDomain>>() {
            @Override
            public Observable<InboxReputationDetailDomain> call(InboxReputationDetailDomain inboxReputationDetailDomain) {
                return getReviewUseCase.createObservable(reviewParam)
                        .flatMap(new Func1<ReviewDomain, Observable<InboxReputationDetailDomain>>() {
                            @Override
                            public Observable<InboxReputationDetailDomain> call(ReviewDomain reviewDomain) {
                                domain.setReviewDomain(reviewDomain);
                                return Observable.just(domain);
                            }
                        });
            }
        };
    }

    public static RequestParams getParam(String reputationId, String userId, int tab) {
        RequestParams params = RequestParams.create();
        params.getParameters().putAll(GetInboxReputationUseCase.getSpecificReputation
                (reputationId, tab).getParameters());
        params.getParameters().putAll(GetReviewUseCase.getParam(reputationId, userId, tab)
                .getParameters());
        return params;
    }
}
