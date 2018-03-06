package com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.DeleteKolCommentUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetKolCommentsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.SendKolCommentUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolComment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.DeleteKolCommentSubscriber;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.GetKolCommentFirstTimeSubscriber;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.GetKolCommentSubscriber;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.SendKolCommentSubscriber;

import javax.inject.Inject;

/**
 * @author by nisie on 10/31/17.
 */

public class KolCommentPresenter extends BaseDaggerPresenter<KolComment.View>
        implements KolComment.Presenter {

    private final GetKolCommentsUseCase getKolCommentsUseCase;
    private final SendKolCommentUseCase sendKolCommentUseCase;
    private final DeleteKolCommentUseCase deleteKolCommentUseCase;

    private String cursor;

    @Override
    public void detachView() {
        super.detachView();
        getKolCommentsUseCase.unsubscribe();
    }

    @Inject
    public KolCommentPresenter(GetKolCommentsUseCase getKolCommentsUseCase,
                               SendKolCommentUseCase sendKolCommentUseCase,
                               DeleteKolCommentUseCase deleteKolCommentUseCase) {
        this.getKolCommentsUseCase = getKolCommentsUseCase;
        this.sendKolCommentUseCase = sendKolCommentUseCase;
        this.deleteKolCommentUseCase = deleteKolCommentUseCase;
    }

    @Override
    public void getCommentFirstTime(int id) {
        getView().showLoading();
        getKolCommentsUseCase.execute(GetKolCommentsUseCase.getFirstTimeParam(id), new
                GetKolCommentFirstTimeSubscriber(getView()));
    }

    @Override
    public void loadMoreComments(int id) {
        getKolCommentsUseCase.execute(GetKolCommentsUseCase.getParam(id, cursor), new
                GetKolCommentSubscriber(getView()));
    }

    @Override
    public void updateCursor(String lastcursor) {
        this.cursor = lastcursor;
    }

    @Override
    public void deleteComment(String id, int adapterPosition) {
        getView().showProgressDialog();
        deleteKolCommentUseCase.execute(DeleteKolCommentUseCase.getParam(Integer.parseInt(id)), new
                DeleteKolCommentSubscriber(getView(), adapterPosition));
    }

    public void sendComment(int id, String comment) {
        if (isValid(comment)) {
            getView().showProgressDialog();
            sendKolCommentUseCase.execute(SendKolCommentUseCase.getParam(id, comment), new
                    SendKolCommentSubscriber(getView()));
        }
    }

    private boolean isValid(String comment) {
        return comment.trim().length() > 0;
    }
}
