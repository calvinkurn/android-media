package com.tokopedia.discovery.newdiscovery.hotlist.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * Created by hangnadi on 9/26/17.
 */

public interface HotlistContract {

    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

    }
}
