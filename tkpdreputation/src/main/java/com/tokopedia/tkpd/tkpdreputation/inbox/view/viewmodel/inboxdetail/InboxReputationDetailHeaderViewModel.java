package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox.InboxReputationTypeFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.ReputationDataViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailHeaderViewModel implements
        Visitable<InboxReputationDetailTypeFactory> {

    String avatarImage;
    String name;
    String deadline;
    ReputationDataViewModel reputationDataViewModel;

    public InboxReputationDetailHeaderViewModel(String avatarImage, String name, String deadline,
                                                ReputationDataViewModel reputationDataViewModel) {
        this.avatarImage = avatarImage;
        this.name = name;
        this.deadline = deadline;
        this.reputationDataViewModel = reputationDataViewModel;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public String getName() {
        return name;
    }

    public String getDeadline() {
        return deadline;
    }

    public ReputationDataViewModel getReputationDataViewModel() {
        return reputationDataViewModel;
    }

    @Override
    public int type(InboxReputationDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
