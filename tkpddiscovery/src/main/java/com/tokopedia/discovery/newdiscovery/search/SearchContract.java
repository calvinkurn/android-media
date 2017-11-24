package com.tokopedia.discovery.newdiscovery.search;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by henrypriyono on 10/10/17.
 */

public interface SearchContract {
    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

    }
}
