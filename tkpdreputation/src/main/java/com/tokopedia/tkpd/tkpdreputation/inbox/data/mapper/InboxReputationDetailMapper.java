package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.InboxReputationDetailPojo;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.ProductData;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.ReputationBadge;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.ReviewData;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.ReviewInboxDatum;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.ReviewResponse;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.ShopData;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.ShopReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.UserData;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.UserReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ReputationBadgeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ImageAttachmentDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailItemDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ProductDataDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ReviewDataDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ShopDataDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ShopReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.UserDataDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.UserReputationDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailMapper implements Func1<Response<TkpdResponse>, InboxReputationDetailDomain> {
    @Override
    public InboxReputationDetailDomain call(Response<TkpdResponse> response) {

        if (response.isSuccessful()) {
            if (!response.body().isNullData()) {
                InboxReputationDetailPojo data = response.body()
                        .convertDataObj(InboxReputationDetailPojo.class);
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
            if (response.body() == null ||
                    (response.body().getErrorMessages() == null
                            && response.body().getErrorMessages().isEmpty())) {
                throw new RuntimeException(String.valueOf(response.code()));
            } else {
                throw new ErrorMessageException(response.body().getErrorMessageJoined());
            }
        }
    }

    private InboxReputationDetailDomain mappingToDomain(InboxReputationDetailPojo data) {
        return new InboxReputationDetailDomain(convertToListReview(data),
                data.getReputationId(),
                convertToUserDataDomain(data.getUserData()),
                convertToShopDataDomain(data.getShopData()),
                data.getInvoiceRefNum(),
                data.getInvoiceTime());
    }

    private ShopDataDomain convertToShopDataDomain(ShopData shopData) {
        return new ShopDataDomain(shopData.getShopId(),
                shopData.getShopUserId(),
                shopData.getDomain(),
                shopData.getShopName(),
                shopData.getShopUrl(),
                shopData.getLogo(),
                convertToShopReputationDomain(shopData.getShopReputation()));
    }

    private ShopReputationDomain convertToShopReputationDomain(ShopReputation shopReputation) {
        return new ShopReputationDomain(
                shopReputation.getTooltip(),
                shopReputation.getReputationScore(),
                shopReputation.getScore(),
                shopReputation.getMinBadgeScore(),
                shopReputation.getReputationBadgeUrl(),
                convertToReputationBadgeDomain(shopReputation.getReputationBadge())
        );
    }

    private ReputationBadgeDomain convertToReputationBadgeDomain(ReputationBadge reputationBadge) {
        return new ReputationBadgeDomain(reputationBadge.getLevel(),
                reputationBadge.getSet());
    }

    private UserDataDomain convertToUserDataDomain(UserData userData) {
        return new UserDataDomain(
                userData.getUserId(),
                userData.getFullName(),
                userData.getUserEmail(),
                userData.getUserStatus(),
                userData.getUserUrl(),
                userData.getUserLabel(),
                userData.getUserProfilePict(),
                convertToUserReputationDomain(userData.getUserReputation())
        );
    }

    private UserReputationDomain convertToUserReputationDomain(UserReputation userReputation) {
        return new UserReputationDomain(
                userReputation.getPositive(),
                userReputation.getNeutral(),
                userReputation.getNegative(),
                userReputation.getPositivePercentage(),
                userReputation.getNoReputation()
        );
    }

    private List<InboxReputationDetailItemDomain> convertToListReview(InboxReputationDetailPojo data) {
        List<InboxReputationDetailItemDomain> list = new ArrayList<>();
        for (ReviewInboxDatum pojo : data.getReviewInboxData()) {
            list.add(convertToReputationItem(pojo));
        }

        return list;
    }

    private InboxReputationDetailItemDomain convertToReputationItem(ReviewInboxDatum pojo) {
        return new InboxReputationDetailItemDomain(
                convertToProductDataDomain(pojo.getProductData()),
                pojo.getReviewInboxId(),
                pojo.getReviewId(),
                pojo.isReviewHasReviewed(),
                pojo.isReviewIsSkippable(),
                pojo.isReviewIsSkipped(),
                pojo.isReviewIsEditable(),
                convertToReviewDataDomain(pojo.getReviewData())
        );
    }

    private ReviewDataDomain convertToReviewDataDomain(ReviewData reviewData) {
        return new ReviewDataDomain(
                reviewData.getReviewId(),
                reviewData.getReputationId(),
                reviewData.getReviewTitle(),
                reviewData.getReviewMessage(),
                reviewData.getReviewRating(),
                convertToImageAttachmentDomain(reviewData.getReviewImageUrl()),
                reviewData.getReviewCreateTime(),
                reviewData.getReviewUpdateTime(),
                reviewData.isReviewAnonymity(),
                convertToReviewResponseDomain(reviewData.getReviewResponse())
        );
    }

    private List<ImageAttachmentDomain> convertToImageAttachmentDomain(List<Object> reviewImageUrl) {
        List<ImageAttachmentDomain> list = new ArrayList<>();
        return list;
    }

    private ReviewResponseDomain convertToReviewResponseDomain(ReviewResponse reviewResponse) {
        return new ReviewResponseDomain(
                reviewResponse.getResponseMessage(),
                reviewResponse.getResponseCreateTime(),
                reviewResponse.getResponseBy()
        );
    }

    private ProductDataDomain convertToProductDataDomain(ProductData productData) {
        return new ProductDataDomain(
                productData.getProductId(),
                productData.getProductName(),
                productData.getProductImageUrl(),
                productData.getProductPageUrl(),
                productData.getShopId(),
                productData.getProductStatus()
        );
    }
}
