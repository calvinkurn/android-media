package com.tokopedia.inbox.rescenter.createreso.view.listener;

import android.app.Fragment;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;

/**
 * Created by yoasfs on 30/08/17.
 */

public interface AttachmentFragmentListener {

    interface View extends CustomerView {
        void populateDataToView();
    }

    interface Presenter extends CustomerPresenter<AttachmentActivityListener.View> {
        void initResultViewModel(ResultViewModel resultViewModel);
    }
}
