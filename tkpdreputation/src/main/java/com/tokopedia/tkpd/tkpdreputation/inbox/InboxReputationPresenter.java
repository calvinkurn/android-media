package com.tokopedia.tkpd.tkpdreputation.inbox;


import com.tokopedia.core.base.presentation.BaseDaggerPresenter;

import javax.inject.Inject;

/**
 * @author by nisie on 8/10/17.
 */

public class InboxReputationPresenter
        extends BaseDaggerPresenter<InboxReputation.View>
        implements InboxReputation.Presenter {

    @Inject
    InboxReputationPresenter(){}
}
