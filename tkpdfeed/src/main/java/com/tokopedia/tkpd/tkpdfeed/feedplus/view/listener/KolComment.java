package com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.SendKolCommentDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolComments;

/**
 * @author by nisie on 10/31/17.
 */

public interface KolComment {

    interface View extends CustomerView {

        void onGoToProfile(String url);

        void showLoading();

        void onErrorGetCommentsFirstTime(String errorMessage);

        void onSuccessGetCommentsFirstTime(KolComments kolComments);

        void removeLoading();

        void loadMoreComments();

        void onSuccessGetComments(KolComments kolComments);

        void onSuccessChangeWishlist();

        void updateCursor(String lastcursor);

        void onErrorLoadMoreComment(String errorMessage);

        void onErrorSendComment(String errorMessage);

        void onSuccessSendComment(SendKolCommentDomain sendKolCommentDomain);

        void dismissProgressDialog();

        void showProgressDialog();

        boolean onDeleteCommentKol(String id, boolean canDeleteComment, int adapterPosition);

        void onErrorDeleteComment(String errorMessage);

        void onSuccessDeleteComment(int adapterPosition);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getCommentFirstTime(int id);

        void loadMoreComments(int id);

        void updateCursor(String lastcursor);

        void deleteComment(String id, int adapterPosition);
    }
}
