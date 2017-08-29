package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.FreeReturnDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.RequireDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.AmountViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.FreeReturnViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.RequireViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionResponseViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionListFragmentPresenter extends BaseDaggerPresenter<SolutionListFragmentListener.View> implements SolutionListFragmentListener.Presenter {

    private SolutionListFragmentListener.View mainView;
    private GetSolutionUseCase getSolutionUseCase;
    private SolutionResponseDomain solutionResponseDomain;
    private ResultViewModel resultViewModel;


    @Inject
    public SolutionListFragmentPresenter(GetSolutionUseCase getSolutionUseCase){
        this.getSolutionUseCase = getSolutionUseCase;
    }

    @Override
    public void attachView(SolutionListFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void initResultViewModel(final ResultViewModel resultViewModel) {
        this.resultViewModel = resultViewModel;
        getSolutionUseCase.execute(getSolutionUseCase.getSolutionUseCaseParams(resultViewModel), new Subscriber<SolutionResponseDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mainView.showErrorToast(e.getLocalizedMessage());

            }

            @Override
            public void onNext(SolutionResponseDomain responseDomain) {
                if (responseDomain != null) {
                    solutionResponseDomain = responseDomain;
                    if (solutionResponseDomain.getRequire() != null) {
                        resultViewModel.isAttachmentRequired = solutionResponseDomain.getRequire().isAttachment();
                    }
                    mainView.populateDataToView(mappingSolutionResponseViewModel(solutionResponseDomain));
                }
            }
        });
    }

    @Override
    public void solutionClicked(SolutionViewModel solutionViewModel) {
        if (solutionViewModel.getAmount() == null) {
            resultViewModel.solution = solutionViewModel.getId();
            mainView.submitData(resultViewModel);
        } else {
            mainView.moveToSolutionDetail(solutionViewModel);
        }
    }

    private SolutionResponseViewModel mappingSolutionResponseViewModel(SolutionResponseDomain domain) {
        return new SolutionResponseViewModel(
                domain.getSolutions() != null ? mappingSolutionViewModelList(domain.getSolutions()) : new ArrayList<SolutionViewModel>(),
                domain.getRequire() != null ? mappingRequireViewModel(domain.getRequire()) : null,
                domain.getFreeReturn() != null ? mappingFreeReturnViewModel(domain.getFreeReturn()) : null);
    }

    private List<SolutionViewModel> mappingSolutionViewModelList(List<SolutionDomain> domainList) {
        List<SolutionViewModel> viewModelList = new ArrayList<>();
        for (SolutionDomain solutionDomain : domainList) {
            viewModelList.add(new SolutionViewModel(
                    solutionDomain.getId(),
                    solutionDomain.getName(),
                    solutionDomain.getAmount() != null ? mappingAmountViewModel(solutionDomain.getAmount()) : null));
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
        return new FreeReturnViewModel(domain.getInfo());
    }

}
