package com.tokopedia.inbox.rescenter.createreso.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.AppealSolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.AppealSolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.FreeReturnDomain;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.AmountViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.FreeReturnViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionResponseViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by yoasfs on 04/09/17.
 */

public class AppealSolutionSubscriber extends Subscriber<AppealSolutionResponseDomain> {
    private final SolutionListFragmentListener.View mainView;

    public AppealSolutionSubscriber(SolutionListFragmentListener.View mainView) {
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
    public void onNext(AppealSolutionResponseDomain appealSolutionResponseDomain) {
        if (appealSolutionResponseDomain != null) {
            mainView.showSuccessGetSolution(
                    mappingSolutionResponseViewModel(
                            appealSolutionResponseDomain));
        }
    }

    private SolutionResponseViewModel mappingSolutionResponseViewModel(
            AppealSolutionResponseDomain domain) {
        return new SolutionResponseViewModel(
                domain.getSolutions() != null ?
                        mappingSolutionViewModelList(domain.getSolutions()) :
                        new ArrayList<SolutionViewModel>(),
                null,
                domain.getFreeReturn() != null ?
                        mappingFreeReturnViewModel(domain.getFreeReturn()) :
                        null);
    }

    private List<SolutionViewModel> mappingSolutionViewModelList(
            List<AppealSolutionDomain> domainList) {
        List<SolutionViewModel> viewModelList = new ArrayList<>();
        for (AppealSolutionDomain solutionDomain : domainList) {
            viewModelList.add(new SolutionViewModel(
                    solutionDomain.getId(),
                    solutionDomain.getName(),
                    solutionDomain.getSolutionName(),
                    solutionDomain.getRefundAmount() != null ?
                            mappingAmountViewModel(solutionDomain.getRefundAmount()) :
                            null));
        }
        return viewModelList;
    }

    private AmountViewModel mappingAmountViewModel(AmountDomain domain) {
        return new AmountViewModel(domain.getIdr(),
                domain.getInteger());
    }

    private FreeReturnViewModel mappingFreeReturnViewModel(FreeReturnDomain domain) {
        return new FreeReturnViewModel(domain.getInfo(), domain.getLink());
    }
}
