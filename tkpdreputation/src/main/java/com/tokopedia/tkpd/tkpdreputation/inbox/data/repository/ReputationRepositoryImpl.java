package com.tokopedia.tkpd.tkpdreputation.inbox.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.factory.ReputationFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationItemDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.PagingDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendReplyReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.FavoriteShopDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.domain.model.ReportReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.CheckShopFavoriteDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewSubmitDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendSmileyReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SkipReviewDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

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
    public Observable<InboxReputationDomain> getInboxReputationFromLocal(RequestParams requestParams) {
        return reputationFactory
                .createLocalInboxReputationDataSource()
                .getInboxReputationFromCache(requestParams)
                .onErrorReturn(new Func1<Throwable, InboxReputationDomain>() {
                    @Override
                    public InboxReputationDomain call(Throwable throwable) {
                        List<InboxReputationItemDomain> list = new ArrayList<>();
                        return new InboxReputationDomain(list,
                                new PagingDomain(false, false));
                    }
                });
    }

    @Override
    public Observable<ReviewDomain> getReviewFromCloud(RequestParams requestParams) {
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

    @Override
    public Observable<ReportReviewDomain> reportReview(RequestParams requestParams) {
        return reputationFactory
                .createCloudReportReviewDataSource()
                .reportReview(requestParams);
    }

    @Override
    public Observable<CheckShopFavoriteDomain> checkIsShopFavorited(RequestParams requestParams) {
        return reputationFactory
                .createCloudCheckShopFavoriteDataSource()
                .checkShopIsFavorited(requestParams);
    }

    @Override
    public Observable<FavoriteShopDomain> favoriteShop(RequestParams requestParams) {
        return reputationFactory
                .createCloudFaveShopDataSource()
                .favoriteShop(requestParams);
    }

    @Override
    public Observable<DeleteReviewResponseDomain> deleteReviewResponse(RequestParams requestParams) {
        return reputationFactory
                .createCloudDeleteReviewResponseDataSource()
                .deleteReviewResponse(requestParams);
    }

    @Override
    public Observable<SendReplyReviewDomain> insertReviewResponse(RequestParams requestParams) {
        return reputationFactory
                .createCloudReplyReviewDataSource()
                .insertReviewResponse(requestParams);
    }

    @Override
    public Observable<GetLikeDislikeReviewDomain> getLikeDislikeReview(RequestParams requestParams) {
        return reputationFactory
                .createCloudGetLikeDislikeDataSource()
                .getLikeDislikeReview(requestParams);
    }

    @Override
    public Observable<LikeDislikeDomain> likeDislikeReview(RequestParams requestParams) {
        return reputationFactory
                .createCloudLikeDislikeDataSource()
                .getLikeDislikeReview(requestParams);
    }
}
