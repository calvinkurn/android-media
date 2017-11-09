package com.tokopedia.inbox.rescenter.createreso.view.listener;

import android.app.Fragment;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;

/**
 * Created by yoasfs on 24/08/17.
 */

public interface SolutionListActivityListener {

    interface View extends CustomerView {
        void inflateFragment(Fragment fragment, String TAG);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initFragment(ResultViewModel resultViewModel);

        void initEditAppealFragment(EditAppealSolutionModel editAppealSolutionModel);
    }
}
