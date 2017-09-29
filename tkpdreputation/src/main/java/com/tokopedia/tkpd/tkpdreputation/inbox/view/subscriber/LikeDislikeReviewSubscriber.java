package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;

import rx.Subscriber;

/**
 * @author by nisie on 9/29/17.
 */

public class LikeDislikeReviewSubscriber extends Subscriber<LikeDislikeDomain> {
    private final InboxReputationDetail.View viewListener;
    private final int adapterPosition;

    public LikeDislikeReviewSubscriber(InboxReputationDetail.View viewListener, int adapterPosition) {
        this.viewListener = viewListener;
        this.adapterPosition = adapterPosition;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoadingDialog();
        viewListener.onErrorLikeDislikeReview(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(LikeDislikeDomain likeDislikeDomain) {
        viewListener.finishLoadingDialog();
        viewListener.onSuccessLikeDislikeReview(adapterPosition,
                likeDislikeDomain.getLikeStatus(),
                likeDislikeDomain.getTotalLike());
    }
}
