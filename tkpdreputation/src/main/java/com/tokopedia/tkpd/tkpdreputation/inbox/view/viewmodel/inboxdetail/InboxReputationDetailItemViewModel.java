package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailItemViewModel implements
        Visitable<InboxReputationDetailTypeFactory> {
    @Override
    public int type(InboxReputationDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
