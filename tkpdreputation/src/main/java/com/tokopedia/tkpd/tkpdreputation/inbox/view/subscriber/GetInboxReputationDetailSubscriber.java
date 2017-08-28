package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ImageAttachmentDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailItemDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageAttachmentViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailItemViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by nisie on 8/19/17.
 */

public class GetInboxReputationDetailSubscriber extends Subscriber<InboxReputationDetailDomain> {

    private final InboxReputationDetail.View viewListener;

    public GetInboxReputationDetailSubscriber(InboxReputationDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetInboxDetail(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(InboxReputationDetailDomain inboxReputationDetailDomain) {
        viewListener.onSuccessGetInboxDetail(
                mappingToListItemViewModel(inboxReputationDetailDomain)
        );
    }

    private List<Visitable> mappingToListItemViewModel(InboxReputationDetailDomain
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
                TextUtils.isEmpty(detailDomain.getReviewData().getReviewUpdateTime()) ?
                        detailDomain.getReviewData().getReviewCreateTime() : detailDomain
                        .getReviewData().getReviewUpdateTime(),
                convertToImageAttachmentViewModel(detailDomain.getReviewData().getReviewImageUrl()),
                detailDomain.getReviewData().getReviewMessage(),
                detailDomain.getReviewData().getReviewRating(),
                detailDomain.isReviewHasReviewed(),
                detailDomain.isReviewIsEditable(),
                detailDomain.isReviewIsSkippable(),
                detailDomain.isReviewIsSkipped()
        );
    }

    private List<ImageAttachmentViewModel> convertToImageAttachmentViewModel(List<ImageAttachmentDomain>
                                                                                     reviewImageUrl) {
        return new ArrayList<>();
    }
}
