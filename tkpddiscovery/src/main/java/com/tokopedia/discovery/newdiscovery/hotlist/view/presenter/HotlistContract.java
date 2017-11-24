package com.tokopedia.discovery.newdiscovery.hotlist.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by hangnadi on 9/26/17.
 */

public interface HotlistContract {

    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

    }
}
