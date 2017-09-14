package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.PostEditSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.subscriber.EditSolutionWithRefundSubscriber;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

import javax.inject.Inject;

/**
 * Created by yoasfs on 28/08/17.
 */

public class SolutionDetailFragmentPresenter
        extends BaseDaggerPresenter<SolutionDetailFragmentListener.View>
        implements SolutionDetailFragmentListener.Presenter {

    SolutionDetailFragmentListener.View mainView;
    SolutionViewModel solutionViewModel;
    ResultViewModel resultViewModel;
    EditAppealSolutionModel editAppealSolutionModel;

    PostEditSolutionUseCase postEditSolutionUseCase;

    @Inject
    public SolutionDetailFragmentPresenter(PostEditSolutionUseCase postEditSolutionUseCase) {
        this.postEditSolutionUseCase = postEditSolutionUseCase;
    }


    @Override
    public void attachView(SolutionDetailFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void initResultViewModel(ResultViewModel resultViewModel, SolutionViewModel solutionViewModel) {
        this.solutionViewModel = solutionViewModel;
        this.resultViewModel = resultViewModel;
        mainView.updateAmountError("Maksimal " + solutionViewModel.getAmount().getIdr());
        mainView.updateBottomButton(resultViewModel.refundAmount);
    }

    @Override
    public void initEditAppealSolutionModel(EditAppealSolutionModel editAppealSolutionModel, SolutionViewModel solutionViewModel) {
        this.editAppealSolutionModel = editAppealSolutionModel;
        this.solutionViewModel = solutionViewModel;
        mainView.updateAmountError("Maksimal " + solutionViewModel.getAmount().getIdr());
        mainView.updateBottomButton(editAppealSolutionModel.refundAmount);
    }

    @Override
    public void onAmountChanged(String amount) {
        if (!amount.equals("")) {
            int intAmount = Integer.parseInt(amount);
            if (intAmount > solutionViewModel.getAmount().getInteger()) {
                if (resultViewModel != null) {
                    resultViewModel.refundAmount = solutionViewModel.getAmount().getInteger();
                } else {
                    editAppealSolutionModel.refundAmount = solutionViewModel.getAmount().getInteger();
                }
                mainView.updatePriceEditText(String.valueOf(solutionViewModel.getAmount().getInteger()));
            } else {
                if (resultViewModel != null) {
                    resultViewModel.refundAmount = intAmount;
                } else {
                    editAppealSolutionModel.refundAmount = intAmount;
                }
            }
            if (resultViewModel != null) {
                mainView.updateBottomButton(resultViewModel.refundAmount);
            } else {
                mainView.updateBottomButton(editAppealSolutionModel.refundAmount);
            }
        }
    }

    @Override
    public void onContinueButtonClicked() {
        if (resultViewModel != null) {
            resultViewModel.solution = solutionViewModel.getId();
            resultViewModel.solutionName = solutionViewModel.getName();
            mainView.submitData(resultViewModel);
        } else {
            editAppealSolutionModel.solution = solutionViewModel.getId();
            editAppealSolutionModel.solutionName = solutionViewModel.getName();
            mainView.showDialogCompleteEditAppeal(editAppealSolutionModel)  ;
        }
    }

    @Override
    public void submitEditAppeal() {
        postEditSolutionUseCase.execute(PostEditSolutionUseCase.
                postEditSolutionUseCaseParams(editAppealSolutionModel.resolutionId,
                        editAppealSolutionModel.solution,
                        editAppealSolutionModel.refundAmount),
                new EditSolutionWithRefundSubscriber(mainView));
    }
}
