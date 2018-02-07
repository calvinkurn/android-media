package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FollowKolDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.FollowKolPostUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;

import rx.Subscriber;

/**
 * @author by nisie on 11/3/17.
 */

public class FollowUnfollowKolSubscriber extends Subscriber<FollowKolDomain> {
    protected final FeedPlus.View view;
    protected final FeedPlus.View.Kol kolListener;
    protected final int rowNumber;
    protected final int id;
    protected final int status;


    public FollowUnfollowKolSubscriber(int id, int status, int rowNumber, FeedPlus.View view,
                                       FeedPlus.View.Kol kolListener) {
        this.view = view;
        this.kolListener = kolListener;
        this.rowNumber = rowNumber;
        this.id = id;
        this.status = status;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.finishLoadingProgress();
        kolListener.onErrorFollowKol(ErrorHandler.getErrorMessage(e), id, status, rowNumber);
    }

    @Override
    public void onNext(FollowKolDomain followKolDomain) {
        view.finishLoadingProgress();
        if (followKolDomain.getStatus() == FollowKolPostUseCase.SUCCESS_STATUS)
            kolListener.onSuccessFollowUnfollowKol(rowNumber);
        else {
            kolListener.onErrorFollowKol(MainApplication.getAppContext().getString(R.string
                    .default_request_error_unknown), id, status, rowNumber);
        }
    }
}
