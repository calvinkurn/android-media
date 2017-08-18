package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.CreateTimeFmt;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.InboxReputationPojo;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.Notification;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.OrderData;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.Paging;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.ReputationBadge;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.ReputationData;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.RevieweeBadge;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.RevieweeData;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.CreateTimeFmtDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationItemDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.NotificationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.OrderDataDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.PagingDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ReputationBadgeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ReputationDataDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.RevieweeBadgeCustomerDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.RevieweeBadgeSellerDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.RevieweeDataDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 8/14/17.
 */

public class InboxReputationMapper implements Func1<Response<TkpdResponse>, InboxReputationDomain> {
    @Override
    public InboxReputationDomain call(Response<TkpdResponse> response) {

        if (response.isSuccessful()) {
            if (!response.body().isNullData()) {
                InboxReputationPojo data = response.body().convertDataObj(InboxReputationPojo.class);
                return mappingToDomain(data);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(MainApplication.getAppContext().getString
                            (R.string.default_request_error_unknown));
                }
            }
        } else {
            if (response.body().getErrorMessages() == null
                    && response.body().getErrorMessages().isEmpty()) {
                throw new RuntimeException(String.valueOf(response.code()));
            } else {
                throw new ErrorMessageException(response.body().getErrorMessageJoined());
            }
        }
    }

    private InboxReputationDomain mappingToDomain(InboxReputationPojo data) {
        return new InboxReputationDomain(

                mappingToListInboxReputation(data.getInboxReputation()),
                mappingToNotification(data.getNotification()),
                mappingToPaging(data.getPaging())
        );
    }

    private PagingDomain mappingToPaging(Paging paging) {
        return new PagingDomain(
                paging.isHasNext(),
                paging.isHasPrev()
        );
    }

    private NotificationDomain mappingToNotification(Notification notification) {
        return new NotificationDomain(
                notification.getUnassessedBuyerReputation(),
                notification.getUnassessedSellerReputation(),
                notification.getUpdatedBuyerReputation()
        );
    }

    private List<InboxReputationItemDomain> mappingToListInboxReputation(List<InboxReputation> inboxReputation) {
        List<InboxReputationItemDomain> list = new ArrayList<>();
        if (!inboxReputation.isEmpty()) {
            for (InboxReputation item : inboxReputation) {
                list.add(new InboxReputationItemDomain(
                        item.getInboxId(),
                        item.getShopId(),
                        item.getUserId(),
                        item.getReputationId(),
                        mappingToOrderData(item.getOrderData()),
                        mappingToRevieweeData(item.getRevieweeData()),
                        mappingToReputationData(item.getReputationData())
                ));
            }
        }
        return list;
    }

    private ReputationDataDomain mappingToReputationData(ReputationData reputationData) {
        return new ReputationDataDomain(
                reputationData.getRevieweeScore(),
                reputationData.getRevieweeScoreStatus(),
                reputationData.isShowRevieweeScore(),
                reputationData.getReviewerScore(),
                reputationData.getReviewerScoreStatus(),
                reputationData.isIsEditable(),
                reputationData.isIsInserted(),
                reputationData.isIsLocked(),
                reputationData.isIsAutoScored(),
                reputationData.isIsCompleted(),
                reputationData.isShowLockingDeadline(),
                reputationData.getLockingDeadlineDays(),
                reputationData.isShowBookmark());
    }

    private OrderDataDomain mappingToOrderData(OrderData orderData) {
        return new OrderDataDomain(
                orderData.getInvoiceRefNum(),
                mappingToCreateTimeFmt(orderData.getCreateTimeFmt()),
                orderData.getInvoiceUrl()
        );
    }

    private CreateTimeFmtDomain mappingToCreateTimeFmt(CreateTimeFmt createTimeFmt) {
        return new CreateTimeFmtDomain(
                createTimeFmt.getDateTimeFmt1(),
                createTimeFmt.getDateTimeFmt1x(),
                createTimeFmt.getDateTimeFmt2(),
                createTimeFmt.getDateTimeFmt3(),
                createTimeFmt.getDateTimeFmt3x(),
                createTimeFmt.getDateFmt1()
        );
    }

    private RevieweeDataDomain mappingToRevieweeData(RevieweeData revieweeData) {
        if (isCustomerBadge(revieweeData.getRevieweeBadge())) {
            return new RevieweeDataDomain(
                    revieweeData.getRevieweeName(),
                    revieweeData.getRevieweeUri(),
                    revieweeData.getRevieweeRole(),
                    revieweeData.getRevieweePicture(),
                    mappingToRevieweeBadgeSeller(revieweeData.getRevieweeBadge())
            );
        } else {
            return new RevieweeDataDomain(
                    revieweeData.getRevieweeName(),
                    revieweeData.getRevieweeUri(),
                    revieweeData.getRevieweeRole(),
                    revieweeData.getRevieweePicture(),
                    mappingToRevieweeBadgeCustomer(revieweeData.getRevieweeBadge())
            );
        }

    }

    private RevieweeBadgeCustomerDomain mappingToRevieweeBadgeCustomer(RevieweeBadge revieweeBadge) {
        return new RevieweeBadgeCustomerDomain(
                revieweeBadge.getPositive(),
                revieweeBadge.getNeutral(),
                revieweeBadge.getNegative(),
                revieweeBadge.getPositive_percentage(),
                revieweeBadge.getNo_reputation()
        );
    }

    private RevieweeBadgeSellerDomain mappingToRevieweeBadgeSeller(RevieweeBadge revieweeBadge) {
        return new RevieweeBadgeSellerDomain(revieweeBadge.getTooltip(),
                revieweeBadge.getReputationScore(),
                revieweeBadge.getScore(),
                revieweeBadge.getMinBadgeScore(),
                revieweeBadge.getReputationBadgeUrl(),
                mappingToReputationBadge(revieweeBadge.getReputationBadge()));
    }

    private boolean isCustomerBadge(RevieweeBadge revieweeBadge) {
        return revieweeBadge.getPositive_percentage() != null;
    }

    private ReputationBadgeDomain mappingToReputationBadge(ReputationBadge reputationBadge) {
        return new ReputationBadgeDomain(
                reputationBadge.getLevel(),
                reputationBadge.getSet()
        );
    }
}
