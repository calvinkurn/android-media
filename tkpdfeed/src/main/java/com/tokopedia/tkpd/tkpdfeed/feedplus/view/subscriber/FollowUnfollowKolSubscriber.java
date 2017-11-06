package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FollowKolDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;

import rx.Subscriber;

/**
 * @author by nisie on 11/3/17.
 */

public class FollowUnfollowKolSubscriber extends Subscriber<FollowKolDomain> {
    protected final FeedPlus.View view;
    protected final FeedPlus.View.Kol kolListener;
    protected final int rowNumber;

    public FollowUnfollowKolSubscriber(int rowNumber, FeedPlus.View view, FeedPlus.View.Kol kolListener) {
        this.view = view;
        this.kolListener = kolListener;
        this.rowNumber = rowNumber;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.finishLoadingProgress();
        kolListener.onErrorFollowKol(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(FollowKolDomain followKolDomain) {
        view.finishLoadingProgress();
        kolListener.onSuccessFollowUnfollowKol(rowNumber);
    }
}
