package com.tokopedia.tkpd.tkpdreputation.inbox.data.factory;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.tome.TomeService;
import com.tokopedia.core.network.apiservices.user.FaveShopActService;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.GetLikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.LikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.data.source.CloudGetLikeDislikeDataSource;
import com.tokopedia.tkpd.tkpdreputation.data.source.CloudLikeDislikeDataSource;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.DeleteReviewResponseMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.FaveShopMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationDetailMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.ReplyReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.ReportReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewSubmitMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewValidateMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendSmileyReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.ShopFavoritedMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SkipReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudCheckShopFavoriteDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudDeleteReviewResponseDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudFaveShopDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudInboxReputationDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudInboxReputationDetailDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudReplyReviewDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudReportReviewDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudSendReviewDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudSendReviewSubmitDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudSendSmileyReputationDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.CloudSkipReviewDataSource;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.source.LocalInboxReputationDataSource;

/**
 * @author by nisie on 8/14/17.
 */

public class ReputationFactory {

    private final ReputationService reputationService;
    private final TomeService tomeService;
    private final FaveShopActService faveShopActService;
    private final InboxReputationMapper inboxReputationMapper;
    private final GlobalCacheManager globalCacheManager;
    private final InboxReputationDetailMapper inboxReputationDetailMapper;
    private final SendSmileyReputationMapper sendSmileyReputationMapper;
    private final SendReviewValidateMapper sendReviewValidateMapper;
    private final SendReviewSubmitMapper sendReviewSubmitMapper;
    private final SkipReviewMapper skipReviewMapper;
    private final ReportReviewMapper reportReviewMapper;
    private final ShopFavoritedMapper shopFavoritedMapper;
    private final FaveShopMapper faveShopMapper;
    private final DeleteReviewResponseMapper deleteReviewResponseMapper;
    private final ReplyReviewMapper replyReviewMapper;
    private final GetLikeDislikeMapper getLikeDislikeMapper;
    private final LikeDislikeMapper likeDislikeMapper;

    public ReputationFactory(TomeService tomeService,
                             ReputationService reputationService,
                             InboxReputationMapper inboxReputationMapper,
                             InboxReputationDetailMapper inboxReputationDetailMapper,
                             SendSmileyReputationMapper sendSmileyReputationMapper,
                             SendReviewValidateMapper sendReviewValidateMapper,
                             SendReviewSubmitMapper sendReviewSubmitMapper,
                             SkipReviewMapper skipReviewMapper,
                             ReportReviewMapper reportReviewMapper,
                             ShopFavoritedMapper shopFavoritedMapper,
                             GlobalCacheManager globalCacheManager,
                             FaveShopActService faveShopActService,
                             FaveShopMapper faveShopMapper,
                             DeleteReviewResponseMapper deleteReviewResponseMapper,
                             ReplyReviewMapper replyReviewMapper,
                             GetLikeDislikeMapper getLikeDislikeMapper,
                             LikeDislikeMapper likeDislikeMapper) {
        this.reputationService = reputationService;
        this.globalCacheManager = globalCacheManager;
        this.inboxReputationMapper = inboxReputationMapper;
        this.inboxReputationDetailMapper = inboxReputationDetailMapper;
        this.sendSmileyReputationMapper = sendSmileyReputationMapper;
        this.sendReviewValidateMapper = sendReviewValidateMapper;
        this.sendReviewSubmitMapper = sendReviewSubmitMapper;
        this.skipReviewMapper = skipReviewMapper;
        this.reportReviewMapper = reportReviewMapper;
        this.shopFavoritedMapper = shopFavoritedMapper;
        this.tomeService = tomeService;
        this.faveShopActService = faveShopActService;
        this.faveShopMapper = faveShopMapper;
        this.deleteReviewResponseMapper = deleteReviewResponseMapper;
        this.replyReviewMapper = replyReviewMapper;
        this.getLikeDislikeMapper = getLikeDislikeMapper;
        this.likeDislikeMapper = likeDislikeMapper;
    }

    public CloudInboxReputationDataSource createCloudInboxReputationDataSource() {
        return new CloudInboxReputationDataSource(reputationService, inboxReputationMapper, globalCacheManager);
    }

    public LocalInboxReputationDataSource createLocalInboxReputationDataSource() {
        return new LocalInboxReputationDataSource(globalCacheManager);
    }

    public CloudInboxReputationDetailDataSource createCloudInboxReputationDetailDataSource() {
        return new CloudInboxReputationDetailDataSource(reputationService,
                inboxReputationDetailMapper);
    }

    public CloudSendSmileyReputationDataSource createCloudSendSmileyReputationDataSource() {
        return new CloudSendSmileyReputationDataSource(reputationService,
                sendSmileyReputationMapper);
    }

    public CloudSendReviewDataSource createCloudSendReviewValidationDataSource() {
        return new CloudSendReviewDataSource(reputationService,
                sendReviewValidateMapper);
    }

    public CloudSendReviewSubmitDataSource createCloudSendReviewSubmitDataSource() {
        return new CloudSendReviewSubmitDataSource(reputationService,
                sendReviewSubmitMapper);
    }

    public CloudSkipReviewDataSource createCloudSkipReviewDataSource() {
        return new CloudSkipReviewDataSource(reputationService,
                skipReviewMapper);
    }

    public CloudReportReviewDataSource createCloudReportReviewDataSource() {
        return new CloudReportReviewDataSource(reputationService,
                reportReviewMapper);
    }

    public CloudCheckShopFavoriteDataSource createCloudCheckShopFavoriteDataSource() {
        return new CloudCheckShopFavoriteDataSource(tomeService, shopFavoritedMapper);
    }

    public CloudFaveShopDataSource createCloudFaveShopDataSource() {
        return new CloudFaveShopDataSource(faveShopActService, faveShopMapper);
    }

    public CloudDeleteReviewResponseDataSource createCloudDeleteReviewResponseDataSource() {
        return new CloudDeleteReviewResponseDataSource(reputationService,
                deleteReviewResponseMapper);
    }

    public CloudReplyReviewDataSource createCloudReplyReviewDataSource() {
        return new CloudReplyReviewDataSource(reputationService,
                replyReviewMapper);
    }

    public CloudGetLikeDislikeDataSource createCloudGetLikeDislikeDataSource() {
        return new CloudGetLikeDislikeDataSource(
                reputationService,
                getLikeDislikeMapper
        );
    }

    public CloudLikeDislikeDataSource createCloudLikeDislikeDataSource() {
        return new CloudLikeDislikeDataSource(
                reputationService,
                likeDislikeMapper
        );
    }
}
