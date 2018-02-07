package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FollowKolDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.FollowKolPostUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;

/**
 * @author by nisie on 11/6/17.
 */

public class FollowUnfollowKolRecommendationSubscriber extends FollowUnfollowKolSubscriber {

    private final int position;

    public FollowUnfollowKolRecommendationSubscriber(int id, int status,
                                                     int rowNumber,
                                                     int position,
                                                     FeedPlus.View view,
                                                     FeedPlus.View.Kol kolListener) {
        super(id, status, rowNumber, view, kolListener);
        this.position = position;
    }

    @Override
    public void onNext(FollowKolDomain followKolDomain) {
        view.finishLoadingProgress();
        if (followKolDomain.getStatus() == FollowKolPostUseCase.SUCCESS_STATUS
                && status == FollowKolPostUseCase.PARAM_FOLLOW)
            kolListener.onSuccessFollowKolFromRecommendation(rowNumber, position);
        else if (followKolDomain.getStatus() == FollowKolPostUseCase.SUCCESS_STATUS
                && status == FollowKolPostUseCase.PARAM_UNFOLLOW){
            kolListener.onSuccessUnfollowKolFromRecommendation(rowNumber, position);
        }
        else if(status == FollowKolPostUseCase.PARAM_FOLLOW){
            kolListener.onErrorFollowKol(MainApplication.getAppContext().getString(R.string
                            .failed_to_follow), id, status, rowNumber);
        }else{
            kolListener.onErrorFollowKol(MainApplication.getAppContext().getString(R.string
                    .failed_to_unfollow), id, status, rowNumber);
        }

    }
}
