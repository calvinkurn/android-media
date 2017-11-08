package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolComment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolComments;

/**
 * @author by nisie on 11/1/17.
 */

public class GetKolCommentFirstTimeSubscriber extends rx.Subscriber<KolComments> {


    private final KolComment.View viewListener;

    public GetKolCommentFirstTimeSubscriber(KolComment.View view) {
        this.viewListener = view;
    }

    @Override
    public void onCompleted() {


    }

    @Override
    public void onError(Throwable e) {
        viewListener.removeLoading();
        viewListener.onErrorGetCommentsFirstTime(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(KolComments kolComments) {
        viewListener.removeLoading();
        viewListener.updateCursor(kolComments.getLastcursor());
        viewListener.onSuccessGetCommentsFirstTime(kolComments);
    }
}
