package com.tokopedia.inbox.rescenter.inboxv2.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by yfsx on 29/01/18.
 */

public interface InboxFilterFragmentListener {


    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

    }
}
