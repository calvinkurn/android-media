package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FollowKolDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;

import rx.Subscriber;

/**
 * @author by nisie on 11/6/17.
 */

public class FollowUnfollowKolRecommendationSubscriber extends FollowUnfollowKolSubscriber {

    private final int position;

    public FollowUnfollowKolRecommendationSubscriber(int rowNumber,
                                                     int position,
                                                     FeedPlus.View view,
                                                     FeedPlus.View.Kol kolListener) {
        super(rowNumber, view, kolListener);
        this.position = position;
    }

    @Override
    public void onNext(FollowKolDomain followKolDomain) {
        view.finishLoadingProgress();
        kolListener.onSuccessFollowUnfollowKolFromRecommendation(rowNumber, position);

    }
}
