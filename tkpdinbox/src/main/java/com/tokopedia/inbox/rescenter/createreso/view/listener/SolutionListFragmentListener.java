package com.tokopedia.inbox.rescenter.createreso.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.SolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionResponseViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

/**
 * Created by yoasfs on 24/08/17.
 */

public interface SolutionListFragmentListener {

    interface View extends CustomerView {
        void populateDataToView(SolutionResponseViewModel solutionResponseViewModel);

        void submitData(ResultViewModel resultViewModel);

        void moveToSolutionDetail(SolutionViewModel solutionViewModel);

        void showSuccessToast();

        void showSuccessGetSolution(SolutionResponseViewModel solutionResponseViewModel);

        void showErrorGetSolution(String error);

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends CustomerPresenter<View> {
        void initResultViewModel(ResultViewModel resultViewModel);

        void updateLocalData(SolutionResponseViewModel solutionResponseViewModel);

        void solutionClicked(SolutionViewModel solutionViewModel);
    }
}
