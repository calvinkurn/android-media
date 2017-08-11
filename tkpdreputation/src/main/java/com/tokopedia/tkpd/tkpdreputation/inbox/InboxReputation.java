package com.tokopedia.tkpd.tkpdreputation.inbox;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * @author by nisie on 8/10/17.
 */

public interface InboxReputation {
    interface View extends CustomerView {
    }

    interface Presenter extends CustomerPresenter<View> {

    }
}