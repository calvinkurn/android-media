package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.LikeKolDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;

import rx.Subscriber;

/**
 * @author by nisie on 11/3/17.
 */

public class LikeKolPostSubscriber extends Subscriber<LikeKolDomain> {
    private final FeedPlus.View view;
    private final FeedPlus.View.Kol kolListener;
    private final int rowNumber;

    public LikeKolPostSubscriber(int rowNumber, FeedPlus.View view, FeedPlus.View.Kol kolListener) {
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
        kolListener.onErrorLikeDislikeKolPost(ErrorHandler.getErrorMessage(e));

    }

    @Override
    public void onNext(LikeKolDomain likeKolDomain) {
        view.finishLoadingProgress();
        if (likeKolDomain.isSuccess()) {
            kolListener.onSuccessLikeDislikeKolPost(rowNumber);
        } else {
            kolListener.onErrorLikeDislikeKolPost(MainApplication.getAppContext().getString(R
                    .string.default_request_error_unknown));
        }
    }
}
