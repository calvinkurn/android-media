package com.tokopedia.discovery.intermediary.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by alifa on 3/24/17.
 */

public interface IntermediaryContract {

    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

    }
}
