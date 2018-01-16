package com.tokopedia.tkpd.tkpdreputation.inbox.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.factory.ReputationFactory;
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
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.DataResponseReviewCount;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewProduct;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewShop;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewstarcount.DataResponseReviewStarCount;

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
    public Observable<InboxReputationDomain> getInboxReputationFromLocal(RequestParams requestParams) {
        return reputationFactory
                .createLocalInboxReputationDataSource()
                .getInboxReputationFromCache(requestParams);
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

    @Override
    public Observable<DataResponseReviewProduct> getReviewProductList(RequestParams requestParams) {
        return reputationFactory
                .createCloudGetReviewProductList()
                .getReviewProductList(requestParams);
    }

    @Override
    public Observable<DataResponseReviewShop> getReviewShopList(RequestParams requestParams) {
        return reputationFactory
                .createCloudGetReviewShopList()
                .getReviewShopList(requestParams);
    }

    @Override
    public Observable<DataResponseReviewHelpful> getReviewHelpful(RequestParams requestParams) {
        return reputationFactory
                .createCloudGetReviewHelpful()
                .getReviewHelpfulList(requestParams);
    }

    @Override
    public Observable<DataResponseReviewStarCount> getReviewStarCount(RequestParams requestParams) {
        return reputationFactory
                .createCloudGetReviewStarCount()
                .getReviewStarCount(requestParams);
    }

    @Override
    public Observable<DataResponseReviewCount> getReviewCount(RequestParams requestParams) {
        return reputationFactory
                .createCloudGetReviewCount()
                .getReviewCount(requestParams);
    }
}
