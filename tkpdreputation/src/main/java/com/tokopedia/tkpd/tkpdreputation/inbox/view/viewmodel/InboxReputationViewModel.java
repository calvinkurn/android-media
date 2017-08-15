package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel;

import java.util.List;

/**
 * @author by nisie on 8/15/17.
 */

public class InboxReputationViewModel {
    List<InboxReputationItemViewModel> list;

    public InboxReputationViewModel(List<InboxReputationItemViewModel> list) {
        this.list = list;
    }

    public List<InboxReputationItemViewModel> getList() {
        return list;
    }
}
