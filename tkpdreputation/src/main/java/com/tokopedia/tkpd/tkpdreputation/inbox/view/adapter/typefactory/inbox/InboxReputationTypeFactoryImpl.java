package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.EmptyReputationSearchViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.InboxReputationViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.EmptySearchModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationItemViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationTypeFactoryImpl extends BaseAdapterTypeFactory
        implements InboxReputationTypeFactory {

    private final InboxReputation.View viewListener;

    public InboxReputationTypeFactoryImpl(InboxReputation.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public int type(InboxReputationItemViewModel viewModel) {
        return InboxReputationViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptySearchModel viewModel) {
        return EmptyReputationSearchViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;

        if (type == InboxReputationViewHolder.LAYOUT)
            viewHolder = new InboxReputationViewHolder(view, viewListener);
        else if (type == EmptyReputationSearchViewHolder.LAYOUT) {
            viewHolder = new EmptyReputationSearchViewHolder(view);
        } else
            viewHolder = super.createViewHolder(view, type);
        return viewHolder;
    }
}
