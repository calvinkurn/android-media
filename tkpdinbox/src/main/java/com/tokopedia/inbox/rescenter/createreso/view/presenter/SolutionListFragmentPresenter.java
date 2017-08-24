package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.RequireDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
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
    public void initResultViewModel(ResultViewModel resultViewModel) {
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
                solutionResponseDomain = responseDomain;
                mainView.populateDataToView(mappingSolutionResponseViewModel(solutionResponseDomain));
            }
        });
    }

    private SolutionResponseViewModel mappingSolutionResponseViewModel(SolutionResponseDomain domain) {
        return new SolutionResponseViewModel(
                domain.getSolutions() != null ? mappingSolutionViewModelList(domain.getSolutions()) : new ArrayList<SolutionViewModel>(),
                domain.getRequire() != null ? mappingRequireViewModel(domain.getRequire()) : null);
    }

    private List<SolutionViewModel> mappingSolutionViewModelList(List<SolutionDomain> domainList) {
        List<SolutionViewModel> viewModelList = new ArrayList<>();
        for (SolutionDomain solutionDomain : domainList) {
            viewModelList.add(new SolutionViewModel(
                    solutionDomain.getId(),
                    solutionDomain.getName(),
                    solutionDomain.getAmount()));
        }
        return viewModelList;
    }

    private RequireViewModel mappingRequireViewModel(RequireDomain domain) {
        return new RequireViewModel(domain.isAttachment());
    }

}
