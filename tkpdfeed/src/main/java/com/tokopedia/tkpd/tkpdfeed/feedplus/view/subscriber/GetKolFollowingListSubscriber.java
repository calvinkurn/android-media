package com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
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

public class GetKolFollowingListSubscriber extends Subscriber<KolFollowingResultDomain> {
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
        mainView.onErrorGetKolFollowingList(ErrorHandler.getErrorMessage(throwable));
    }

    @Override
    public void onNext(KolFollowingResultDomain kolFollowingResultDomain) {
        mainView.hideLoading();
        if (kolFollowingResultDomain.getKolFollowingDomainList().size() != 0) {
            mainView.onSuccessGetKolFollowingList(mappingViewModel(kolFollowingResultDomain));
        } else {
            mainView.onSuccessGetKolFollowingListEmptyState();
        }
    }

    public static KolFollowingResultViewModel mappingViewModel(KolFollowingResultDomain domain) {
        return new KolFollowingResultViewModel(
                domain.isCanLoadMore(),
                domain.getLastCursor(),
                mappingViewModels(domain.getKolFollowingDomainList()),
                domain.getButtonText(),
                domain.getButtonApplink());
    }

    private static List<KolFollowingViewModel> mappingViewModels(List<KolFollowingDomain> domainList) {
        List<KolFollowingViewModel> viewModelList = new ArrayList<>();
        for (KolFollowingDomain domain : domainList) {
            KolFollowingViewModel viewModel = new KolFollowingViewModel(
                    domain.getId(),
                    domain.getAvatarUrl(),
                    domain.getProfileApplink(),
                    domain.getProfileUrl(),
                    domain.isInfluencer(),
                    domain.getName());
            viewModelList.add(viewModel);
        }
        return viewModelList;
    }
}
