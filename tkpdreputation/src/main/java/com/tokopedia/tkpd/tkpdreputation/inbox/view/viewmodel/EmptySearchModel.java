package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox.InboxReputationTypeFactory;

/**
 * @author by nisie on 9/13/17.
 */

public class EmptySearchModel implements Visitable<InboxReputationTypeFactory> {

    @Override
    public int type(InboxReputationTypeFactory inboxReputationTypeFactory) {
        return inboxReputationTypeFactory.type(this);
    }
}