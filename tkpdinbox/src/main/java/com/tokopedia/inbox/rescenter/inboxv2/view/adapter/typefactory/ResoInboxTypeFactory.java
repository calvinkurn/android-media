package com.tokopedia.inbox.rescenter.inboxv2.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.EmptyInboxFilterDataModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.FilterListViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemViewModel;

/**
 * Created by yoasfs on 23/10/17.
 */

public interface ResoInboxTypeFactory {

    int type(EmptyModel emptyModel);

    int type(InboxItemViewModel viewModel);

    int type(EmptyInboxFilterDataModel emptyInboxFilterDataModel);

    int type(FilterListViewModel filterListViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
