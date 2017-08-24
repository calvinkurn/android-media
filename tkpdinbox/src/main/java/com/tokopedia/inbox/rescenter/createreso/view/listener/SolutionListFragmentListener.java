package com.tokopedia.inbox.rescenter.createreso.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;

/**
 * Created by yoasfs on 24/08/17.
 */

public interface SolutionListFragmentListener {

    interface View extends CustomerView {
        void populateDataToView(ResultViewModel resultViewModel);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initResultViewModel(ResultViewModel resultViewModel);
    }
}
