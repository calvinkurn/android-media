package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.KolFollowingDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolFollowingList;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolFollowingViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by yfsx on 28/12/17.
 */

public class GetKolFollowingListSubscriber extends Subscriber<List<KolFollowingDomain>> {
    private KolFollowingList.View mainView;

    public GetKolFollowingListSubscriber(KolFollowingList.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        mainView.hideLoading();
        mainView.onErrorGetKolFollowingList(new MessageErrorException(throwable.getLocalizedMessage()).toString());
    }

    @Override
    public void onNext(List<KolFollowingDomain> kolFollowingDomains) {
        mainView.hideLoading();
        mainView.onSuccessGetKolFollowingList(mappingViewModels(kolFollowingDomains));
    }

    private List<KolFollowingViewModel> mappingViewModels(List<KolFollowingDomain> domainList) {
        List<KolFollowingViewModel> viewModelList = new ArrayList<>();
        for (KolFollowingDomain domain : domainList) {
            KolFollowingViewModel viewModel = new KolFollowingViewModel(
                    domain.getId(),
                    domain.getAvatarUrl(),
                    domain.isVerified(),
                    domain.getName());
            viewModelList.add(viewModel);
        }
        return viewModelList;
    }
}
