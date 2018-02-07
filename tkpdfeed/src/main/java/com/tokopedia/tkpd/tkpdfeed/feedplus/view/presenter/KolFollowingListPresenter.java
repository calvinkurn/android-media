package com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetKolFollowingListLoadMoreUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetKolFollowingListUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolFollowingList;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.GetKolFollowingListLoadMoreSubscriber;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.GetKolFollowingListSubscriber;

import javax.inject.Inject;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingListPresenter extends BaseDaggerPresenter<KolFollowingList.View>
        implements KolFollowingList.Presenter {

    private KolFollowingList.View mainView;
    private final GetKolFollowingListUseCase getKolFollowingListUseCase;
    private final GetKolFollowingListLoadMoreUseCase getKolFollowingListLoadMoreUseCase;

    @Inject
    public KolFollowingListPresenter(GetKolFollowingListUseCase getKolFollowingListUseCase,
                                     GetKolFollowingListLoadMoreUseCase getKolFollowingListLoadMoreUseCase) {
        this.getKolFollowingListUseCase = getKolFollowingListUseCase;
        this.getKolFollowingListLoadMoreUseCase = getKolFollowingListLoadMoreUseCase;
    }

    @Override
    public void getKolFollowingList(int userId) {
        mainView.showLoading();
        getKolFollowingListUseCase.execute(GetKolFollowingListUseCase.getParam(userId),
                new GetKolFollowingListSubscriber(mainView));
    }

    @Override
    public void getKolLoadMore(int userId, String cursor) {
        getKolFollowingListLoadMoreUseCase.execute(GetKolFollowingListLoadMoreUseCase.getParam(userId, cursor),
                new GetKolFollowingListLoadMoreSubscriber(mainView));

    }

    @Override
    public void attachView(KolFollowingList.View view) {
        super.attachView(view);
        this.mainView = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        getKolFollowingListUseCase.unsubscribe();
        getKolFollowingListLoadMoreUseCase.unsubscribe();
    }
}
