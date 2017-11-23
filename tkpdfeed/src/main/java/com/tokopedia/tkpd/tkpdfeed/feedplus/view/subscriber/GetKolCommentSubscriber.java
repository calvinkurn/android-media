package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolComment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolComments;

import rx.Subscriber;

/**
 * @author by nisie on 11/3/17.
 */

public class GetKolCommentSubscriber extends Subscriber<KolComments> {

    private final KolComment.View viewListener;

    public GetKolCommentSubscriber(KolComment.View view) {
        this.viewListener = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.removeLoading();
        viewListener.onErrorLoadMoreComment(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(KolComments kolComments) {
        viewListener.updateCursor(kolComments.getLastcursor());
        viewListener.onSuccessGetComments(kolComments);
    }
}
