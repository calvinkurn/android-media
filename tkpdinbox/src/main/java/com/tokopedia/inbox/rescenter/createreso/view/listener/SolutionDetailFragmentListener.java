package com.tokopedia.inbox.rescenter.createreso.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

/**
 * Created by yoasfs on 24/08/17.
 */

public interface SolutionDetailFragmentListener {

    interface View extends CustomerView {

        void populateDataToView(ResultViewModel resultViewModel, SolutionViewModel solutionViewModel);

        void updateAmountError(String message);

        void updateBottomButton(int refundAmount);

        void submitData(ResultViewModel resultViewModel);

        void showDialogCompleteEditAppeal(EditAppealSolutionModel editAppealSolutionModel);

        void updatePriceEditText(String price);

        void showErrorToast(String error);

        void successEditSolution(String message);

        void errorEditSolution(String error);

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends CustomerPresenter<View> {

        void initResultViewModel(ResultViewModel resultViewModel, SolutionViewModel solutionViewModel);

        void initEditAppealSolutionModel(EditAppealSolutionModel editAppealSolutionModel, SolutionViewModel solutionViewModel);

        void onAmountChanged(String amount);

        void onContinueButtonClicked();

        void submitEditAppeal();
    }
}
