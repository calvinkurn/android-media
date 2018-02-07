package com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory.FeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.KolCommentSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.KolSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.CheckFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.DeleteKolCommentDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FollowKolDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.KolFollowingResultDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.LikeKolDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.SendKolCommentDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feeddetail.DataFeedDetailDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.recentview.RecentViewProductDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolComments;

import java.util.List;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class FeedRepositoryImpl implements FeedRepository {

    private final KolSource kolSource;
    private FeedFactory feedFactory;
    private KolCommentSource kolCommentSource;

    public FeedRepositoryImpl(FeedFactory feedFactory,
                              KolCommentSource kolCommentSource,
                              KolSource kolSource) {
        this.feedFactory = feedFactory;
        this.kolCommentSource = kolCommentSource;
        this.kolSource = kolSource;
    }

    @Override
    public Observable<FeedResult> getFeedsFromCloud(RequestParams requestParams) {
        return feedFactory.createCloudFeedDataSource().getNextPageFeedsList(requestParams);
    }

    @Override
    public Observable<FeedResult> getFirstPageFeedsFromCloud(RequestParams requestParams) {
        return feedFactory.createCloudFirstFeedDataSource().getFirstPageFeedsList(requestParams);
    }

    @Override
    public Observable<FeedResult> getFirstPageFeedsFromLocal() {
        return feedFactory.createLocalFeedDataSource().getFeeds();
    }

    @Override
    public Observable<List<DataFeedDetailDomain>> getFeedsDetail(RequestParams requestParams) {
        return feedFactory.createCloudDetailFeedDataSource().getFeedsDetailList(requestParams);
    }

    @Override
    public Observable<List<RecentViewProductDomain>> getRecentViewProduct(RequestParams requestParams) {
        return feedFactory.createCloudRecentViewedProductSource().getRecentProduct(requestParams);
    }

    @Override
    public Observable<CheckFeedDomain> checkNewFeed(RequestParams parameters) {
        return feedFactory.createCloudCheckNewFeedDataSource().checkNewFeed(parameters);
    }

    @Override
    public Observable<KolComments> getKolComments(RequestParams requestParams) {
        return kolCommentSource.getComments(requestParams);
    }

    @Override
    public Observable<SendKolCommentDomain> sendKolComment(RequestParams requestParams) {
        return kolCommentSource.sendComment(requestParams);
    }

    @Override
    public Observable<LikeKolDomain> likeUnlikeKolPost(RequestParams requestParams) {
        return kolSource.likeKolPost(requestParams);
    }

    @Override
    public Observable<FollowKolDomain> followUnfollowKol(RequestParams requestParams) {
        return kolSource.followKolPost(requestParams);
    }

    @Override
    public Observable<DeleteKolCommentDomain> deleteKolComment(RequestParams requestParams) {
        return kolCommentSource.deleteKolComment(requestParams);
    }

    @Override
    public Observable<KolFollowingResultDomain> getKolFollowingList(RequestParams requestParams) {
        return kolSource.getKolFollowingList(requestParams);
    }
}
