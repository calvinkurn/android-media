package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox;

import android.view.View;

import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.EmptySearchModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationItemViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public interface InboxReputationTypeFactory {

    int type(InboxReputationItemViewModel viewModel);

    int type(EmptySearchModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
