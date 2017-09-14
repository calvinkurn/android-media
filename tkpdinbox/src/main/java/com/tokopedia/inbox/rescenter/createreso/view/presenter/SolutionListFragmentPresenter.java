package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.FreeReturnDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.RequireDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetEditSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.PostEditSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.subscriber.EditSolutionSubscriber;
import com.tokopedia.inbox.rescenter.createreso.view.subscriber.SolutionSubscriber;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.AmountViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
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

public class SolutionListFragmentPresenter
        extends BaseDaggerPresenter<SolutionListFragmentListener.View>
        implements SolutionListFragmentListener.Presenter {

    private SolutionListFragmentListener.View mainView;
    private GetSolutionUseCase getSolutionUseCase;
    private GetEditSolutionUseCase getEditSolutionUseCase;
    private PostEditSolutionUseCase postEditSolutionUseCase;

    private ResultViewModel resultViewModel;
    private EditAppealSolutionModel editAppealSolutionModel;

    private boolean isEditAppeal;


    @Inject
    public SolutionListFragmentPresenter(GetSolutionUseCase getSolutionUseCase,
                                         GetEditSolutionUseCase getEditSolutionUseCase,
                                         PostEditSolutionUseCase postEditSolutionUseCase) {
        this.getSolutionUseCase = getSolutionUseCase;
        this.getEditSolutionUseCase = getEditSolutionUseCase;
        this.postEditSolutionUseCase = postEditSolutionUseCase;
    }

    @Override
    public void attachView(SolutionListFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void initResultViewModel(final ResultViewModel resultViewModel) {
        this.resultViewModel = resultViewModel;
        mainView.showLoading();
        getSolutionUseCase.execute(getSolutionUseCase.getSolutionUseCaseParams(resultViewModel),
                new SolutionSubscriber(mainView));
    }

    @Override
    public void initEditAppeal(EditAppealSolutionModel editAppealSolutionModel) {
        this.editAppealSolutionModel = editAppealSolutionModel;
        mainView.showLoading();
        isEditAppeal = true;
        getEditSolutionUseCase.execute(getEditSolutionUseCase.getEditSolutionUseCaseParams(
                editAppealSolutionModel.resolutionId),
                new EditSolutionSubscriber(mainView));
    }

    @Override
    public void updateLocalData(SolutionResponseViewModel solutionResponseViewModel) {
        if (solutionResponseViewModel.getRequire() != null) {
            resultViewModel.isAttachmentRequired = solutionResponseViewModel.getRequire().isAttachment();
        }
    }

    @Override
    public void submitEditAppeal(SolutionViewModel solutionViewModel) {
        editAppealSolutionModel.solutionName = solutionViewModel.getName();
        editAppealSolutionModel.solution = solutionViewModel.getId();
        postEditSolutionUseCase.execute(PostEditSolutionUseCase.
                postEditSolutionUseCaseParamsWithoutRefund(
                        editAppealSolutionModel.resolutionId,
                        editAppealSolutionModel.solution));
    }

    @Override
    public void solutionClicked(SolutionViewModel solutionViewModel) {
        if (solutionViewModel.getAmount() == null) {
            if (!isEditAppeal) {
                resultViewModel.solution = solutionViewModel.getId();
                resultViewModel.solutionName = solutionViewModel.getName();
                resultViewModel.refundAmount = 0;
                mainView.submitData(resultViewModel);
            } else {
                mainView.showDialogCompleteEditAppeal(solutionViewModel);
            }
        } else {
            mainView.moveToSolutionDetail(solutionViewModel);
        }

    }

}
