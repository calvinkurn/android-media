package com.tokopedia.inbox.rescenter.createreso.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionResponseViewModel;

/**
 * Created by yoasfs on 24/08/17.
 */

public interface SolutionListFragmentListener {

    interface View extends CustomerView {
        void populateDataToView(SolutionResponseViewModel solutionResponseViewModel);

        void showSuccessToast();

        void showErrorToast(String error);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initResultViewModel(ResultViewModel resultViewModel);
    }
}
