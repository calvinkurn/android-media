package com.tokopedia.inbox.rescenter.inboxv2.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.viewholder.visitable.EmptyResoInboxFilterViewHolder;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.viewholder.visitable.EmptyResoInboxViewHolder;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.viewholder.visitable.InboxItemViewHolder;
import com.tokopedia.inbox.rescenter.inboxv2.view.adapter.viewholder.visitable.QuickFilterViewHolder;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.EmptyInboxFilterDataModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.FilterListViewModel;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemViewModel;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ResoInboxTypeFactoryImpl extends BaseAdapterTypeFactory implements ResoInboxTypeFactory {
    ResoInboxFragmentListener.View mainView;

    public ResoInboxTypeFactoryImpl(ResoInboxFragmentListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public int type(EmptyModel emptyModel) {
        return EmptyResoInboxViewHolder.LAYOUT;
    }


    @Override
    public int type(EmptyInboxFilterDataModel emptyInboxFilterDataModel) {
        return EmptyResoInboxFilterViewHolder.LAYOUT;
    }

    @Override
    public int type(FilterListViewModel filterListViewModel) {
        return QuickFilterViewHolder.LAYOUT;
    }

    @Override
    public int type(InboxItemViewModel viewModel) {
        return InboxItemViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;
        if (type == InboxItemViewHolder.LAYOUT) {
            viewHolder = new InboxItemViewHolder(view, mainView);
        } else if(type == EmptyResoInboxViewHolder.LAYOUT) {
            viewHolder = new EmptyResoInboxViewHolder(view, mainView);
        } else if(type == EmptyResoInboxFilterViewHolder.LAYOUT) {
            viewHolder = new EmptyResoInboxFilterViewHolder(view, mainView);
        } else if(type == QuickFilterViewHolder.LAYOUT) {
            viewHolder = new QuickFilterViewHolder(view, mainView);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }

}
