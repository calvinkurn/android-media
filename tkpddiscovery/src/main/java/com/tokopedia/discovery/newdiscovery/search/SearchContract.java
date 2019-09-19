package com.tokopedia.discovery.newdiscovery.search;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by henrypriyono on 10/10/17.
 */

public interface SearchContract {
    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

    }
}
