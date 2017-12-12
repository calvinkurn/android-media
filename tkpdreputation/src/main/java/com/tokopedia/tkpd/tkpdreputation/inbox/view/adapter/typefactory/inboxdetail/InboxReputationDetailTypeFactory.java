package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inboxdetail;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailItemViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public interface InboxReputationDetailTypeFactory {

    int type(InboxReputationDetailHeaderViewModel model);

    int type(InboxReputationDetailItemViewModel model);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
