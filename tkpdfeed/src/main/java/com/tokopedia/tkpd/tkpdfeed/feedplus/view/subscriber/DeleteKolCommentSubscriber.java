package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.DeleteKolCommentDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolComment;

import rx.Subscriber;

/**
 * @author by nisie on 11/10/17.
 */

public class DeleteKolCommentSubscriber extends Subscriber<DeleteKolCommentDomain> {

    private final KolComment.View view;
    private final int adapterPosition;

    public DeleteKolCommentSubscriber(KolComment.View view, int adapterPosition) {
        this.view = view;
        this.adapterPosition = adapterPosition;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.dismissProgressDialog();
        view.onErrorDeleteComment(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(DeleteKolCommentDomain deleteKolCommentDomain) {
        view.dismissProgressDialog();
        if(deleteKolCommentDomain.isSuccess()) {
            view.onSuccessDeleteComment(adapterPosition);
        }
        else{
            view.onErrorDeleteComment(MainApplication.getAppContext().getString(R.string.default_request_error_unknown));

        }
    }
}
