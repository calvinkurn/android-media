package com.tokopedia.inbox.rescenter.createreso.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

/**
 * Created by yoasfs on 24/08/17.
 */

public interface SolutionDetailFragmentListener {

    interface View extends CustomerView {

        void populateDataToView(ResultViewModel resultViewModel, SolutionViewModel solutionViewModel);

        void updateAmountError(String message);

        void updateBottomButton(ResultViewModel resultViewModel);

        void submitData(ResultViewModel resultViewModel);

        void showSuccessToast();

        void showErrorToast(String error);
    }

    interface Presenter extends CustomerPresenter<View> {

        void initResultViewModel(ResultViewModel resultViewModel, SolutionViewModel solutionViewModel);

        void onAmountChanged(String amount);

        void onContinueButtonClicked();
    }
}
