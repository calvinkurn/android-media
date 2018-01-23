package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.KolFollowingResultDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolFollowingList;

import rx.Subscriber;

/**
 * Created by yfsx on 28/12/17.
 */

public class GetKolFollowingListLoadMoreSubscriber extends Subscriber<KolFollowingResultDomain> {
    private KolFollowingList.View mainView;

    public GetKolFollowingListLoadMoreSubscriber(KolFollowingList.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        mainView.hideLoading();
        mainView.onErrorLoadMoreKolFollowingList(ErrorHandler.getErrorMessage(throwable));
    }

    @Override
    public void onNext(KolFollowingResultDomain kolFollowingResultDomain) {
        mainView.hideLoading();
        mainView.onSuccessLoadMoreKolFollowingList(GetKolFollowingListSubscriber.mappingViewModel(kolFollowingResultDomain));
    }

}
