package com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetKolFollowingListUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolFollowingList;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.GetKolFollowingListSubscriber;

import javax.inject.Inject;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingListPresenter extends BaseDaggerPresenter<KolFollowingList.View>
        implements KolFollowingList.Presenter {

    private KolFollowingList.View mainView;
    private final GetKolFollowingListUseCase getKolFollowingListUseCase;

    private String cursor;
    @Inject
    public KolFollowingListPresenter(GetKolFollowingListUseCase getKolFollowingListUseCase) {
        this.getKolFollowingListUseCase = getKolFollowingListUseCase;
        this.cursor = "";
    }

    @Override
    public void getKolFollowingList(int userId) {
        mainView.showLoading();
        getKolFollowingListUseCase.execute(GetKolFollowingListUseCase.getParam(userId, cursor),
                new GetKolFollowingListSubscriber(mainView));
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
    }
}
