package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ReputationBadgeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ImageAttachmentDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailItemDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ShopReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.UserReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageAttachmentViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailItemViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ReputationBadgeViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.RevieweeBadgeCustomerViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.RevieweeBadgeSellerViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 8/19/17.
 */

public class GetInboxReputationDetailSubscriber extends Subscriber<InboxReputationDetailDomain> {

    protected final InboxReputationDetail.View viewListener;

    public GetInboxReputationDetailSubscriber(InboxReputationDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoading();
        viewListener.onErrorGetInboxDetail(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(InboxReputationDetailDomain inboxReputationDetailDomain) {
        viewListener.finishLoading();
        viewListener.onSuccessGetInboxDetail(
                convertToRevieweeBadgeCustomerViewModel(inboxReputationDetailDomain.getUserData()
                        .getUserReputation()),
                convertToRevieweeBadgeSellerViewModel(inboxReputationDetailDomain.getShopData()
                        .getShopReputation()),
                mappingToListItemViewModel(inboxReputationDetailDomain)
        );
    }

    protected RevieweeBadgeSellerViewModel convertToRevieweeBadgeSellerViewModel
            (ShopReputationDomain shopReputationDomain) {
        return new RevieweeBadgeSellerViewModel(
                shopReputationDomain.getTooltip(),
                shopReputationDomain.getReputationScore(),
                shopReputationDomain.getScore(),
                shopReputationDomain.getMinBadgeScore(),
                shopReputationDomain.getReputationBadgeUrl(),
                convertToReputationBadgeViewModel(shopReputationDomain.getReputationBadge())
        );
    }

    private ReputationBadgeViewModel convertToReputationBadgeViewModel(ReputationBadgeDomain reputationBadge) {
        return new ReputationBadgeViewModel(reputationBadge.getLevel(),
                reputationBadge.getSet());
    }

    protected RevieweeBadgeCustomerViewModel convertToRevieweeBadgeCustomerViewModel
            (UserReputationDomain userReputationDomain) {
        return new RevieweeBadgeCustomerViewModel(
                userReputationDomain.getPositive(),
                userReputationDomain.getNeutral(),
                userReputationDomain.getNegative(),
                userReputationDomain.getPositivePercentage(),
                userReputationDomain.getNoReputation()
        );
    }

    protected List<Visitable> mappingToListItemViewModel(InboxReputationDetailDomain
                                                                 inboxReputationDetailDomain) {
        List<Visitable> list = new ArrayList<>();
        if (inboxReputationDetailDomain.getData() != null) {
            for (InboxReputationDetailItemDomain detailDomain : inboxReputationDetailDomain.getData()) {
                list.add(convertToInboxReputationDetailItemViewModel(inboxReputationDetailDomain,
                        detailDomain));
            }
        }
        return list;

    }

    private Visitable convertToInboxReputationDetailItemViewModel(
            InboxReputationDetailDomain inboxDomain, InboxReputationDetailItemDomain detailDomain) {
        return new InboxReputationDetailItemViewModel(
                String.valueOf(detailDomain.getProductData().getProductId()),
                detailDomain.getProductData().getProductName(),
                detailDomain.getProductData().getProductImageUrl(),
                String.valueOf(detailDomain.getReviewData().getReviewId()),
                inboxDomain.getUserData().getFullName(),
                TextUtils.isEmpty(detailDomain.getReviewData().getReviewUpdateTime().getDateTimeAndroid()) ?
                        detailDomain.getReviewData().getReviewCreateTime().getDateTimeAndroid() : detailDomain
                        .getReviewData().getReviewUpdateTime().getDateTimeAndroid(),
                convertToImageAttachmentViewModel(detailDomain.getReviewData().getReviewImageUrl()),
                detailDomain.getReviewData().getReviewMessage(),
                detailDomain.getReviewData().getReviewRating(),
                detailDomain.isReviewHasReviewed(),
                detailDomain.isReviewIsEditable(),
                detailDomain.isReviewIsSkippable(),
                detailDomain.isReviewIsSkipped(),
                detailDomain.getProductData().getShopId(),
                viewListener.getTab()
        );
    }

    private List<ImageAttachmentViewModel>
    convertToImageAttachmentViewModel(List<ImageAttachmentDomain> reviewImageUrl) {
        List<ImageAttachmentViewModel> list = new ArrayList<>();
        for (ImageAttachmentDomain domain : reviewImageUrl) {
            list.add(new ImageAttachmentViewModel(
                    domain.getAttachmentId(),
                    domain.getDescription(),
                    domain.getUriThumbnail(),
                    domain.getUriLarge()
            ));
        }
        return list;
    }
}
