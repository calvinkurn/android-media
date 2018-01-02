package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.KolFollowingDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.KolFollowingResultDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.KolFollowingList;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolFollowingResultViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolFollowingViewModel;

import java.util.ArrayList;
import java.util.List;

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
        mainView.onErrorLoadMoreKolFollowingList(new MessageErrorException(throwable.getLocalizedMessage()).toString());
    }

    @Override
    public void onNext(KolFollowingResultDomain kolFollowingResultDomain) {
        mainView.hideLoading();
        mainView.onSuccessLoadMoreKolFollowingList(mappingViewModel(kolFollowingResultDomain));
    }

    private KolFollowingResultViewModel mappingViewModel(KolFollowingResultDomain domain) {
        return new KolFollowingResultViewModel(
                domain.isCanLoadMore(),
                domain.getLastCursor(),
                mappingViewModels(domain.getKolFollowingDomainList()));
    }

    private List<KolFollowingViewModel> mappingViewModels(List<KolFollowingDomain> domainList) {
        List<KolFollowingViewModel> viewModelList = new ArrayList<>();
        for (KolFollowingDomain domain : domainList) {
            KolFollowingViewModel viewModel = new KolFollowingViewModel(
                    domain.getId(),
                    domain.getAvatarUrl(),
                    domain.getProfileApplink(),
                    domain.isInfluencer(),
                    domain.getName());
            viewModelList.add(viewModel);
        }
        return viewModelList;
    }
}
