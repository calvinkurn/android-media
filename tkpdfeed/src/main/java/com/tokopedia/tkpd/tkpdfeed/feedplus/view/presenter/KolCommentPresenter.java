package com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetKolCommentsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolComment;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolCommentViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolComments;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 10/31/17.
 */

public class KolCommentPresenter extends BaseDaggerPresenter<KolComment.View>
        implements KolComment.Presenter {

    private final GetKolCommentsUseCase getKolCommentsUseCase;

    @Override
    public void detachView() {
        super.detachView();
        getKolCommentsUseCase.unsubscribe();
    }

    @Inject
    public KolCommentPresenter(GetKolCommentsUseCase getKolCommentsUseCase) {
        this.getKolCommentsUseCase = getKolCommentsUseCase;
    }

    @Override
    public void getCommentFirstTime() {
//        getView().showLoading();
//        getKolCommentsUseCase.execute(GetKolCommentsUseCase.getFirstTimeParam(), new
//                GetKolCommentFirstTimeSubscriber(getView()));
        getView().onSuccessGetCommentsFirstTime(new KolComments(new ArrayList<KolCommentViewModel>()));
    }

    @Override
    public void loadMoreComments() {
        getView().onSuccessGetComments(new KolComments(new ArrayList<KolCommentViewModel>()));

    }

    @Override
    public void changeWishlist() {
        getView().onSuccessChangeWishlist();

    }
}
