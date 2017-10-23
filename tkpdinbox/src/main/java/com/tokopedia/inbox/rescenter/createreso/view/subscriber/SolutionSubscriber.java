package com.tokopedia.inbox.rescenter.createreso.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.FreeReturnDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.RequireDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.AmountViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.FreeReturnViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.RequireViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionResponseViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by yoasfs on 04/09/17.
 */

public class SolutionSubscriber extends Subscriber<SolutionResponseDomain> {
    private final SolutionListFragmentListener.View mainView;

    public SolutionSubscriber(SolutionListFragmentListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        mainView.showErrorGetSolution(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(SolutionResponseDomain solutionResponseDomain) {
        if (solutionResponseDomain != null) {
            mainView.showSuccessGetSolution(mappingSolutionResponseViewModel(solutionResponseDomain));
        }
    }

    private SolutionResponseViewModel mappingSolutionResponseViewModel(SolutionResponseDomain domain) {
        return new SolutionResponseViewModel(
                domain.getSolutions() != null ?
                        mappingSolutionViewModelList(domain.getSolutions()) :
                        new ArrayList<SolutionViewModel>(),
                domain.getRequire() != null ?
                        mappingRequireViewModel(domain.getRequire()) :
                        null,
                domain.getFreeReturn() != null ?
                        mappingFreeReturnViewModel(domain.getFreeReturn()) :
                        null);
    }

    private List<SolutionViewModel> mappingSolutionViewModelList(List<SolutionDomain> domainList) {
        List<SolutionViewModel> viewModelList = new ArrayList<>();
        for (SolutionDomain solutionDomain : domainList) {
            viewModelList.add(new SolutionViewModel(
                    solutionDomain.getId(),
                    solutionDomain.getName(),
                    solutionDomain.getSolutionName(),
                    solutionDomain.getAmount() != null ?
                            mappingAmountViewModel(solutionDomain.getAmount()) :
                            null));
        }
        return viewModelList;
    }

    private RequireViewModel mappingRequireViewModel(RequireDomain domain) {
        return new RequireViewModel(domain.isAttachment());
    }

    private AmountViewModel mappingAmountViewModel(AmountDomain domain) {
        return new AmountViewModel(domain.getIdr(), domain.getInteger());
    }

    private FreeReturnViewModel mappingFreeReturnViewModel(FreeReturnDomain domain) {
        return new FreeReturnViewModel(domain.getInfo(), domain.getLink());
    }
}
