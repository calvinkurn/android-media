package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inboxdetail;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ReputationAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.inboxdetail.InboxReputationDetailHeaderViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.inboxdetail.InboxReputationDetailItemViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationDetailFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailItemViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailTypeFactoryImpl extends BaseAdapterTypeFactory
        implements InboxReputationDetailTypeFactory {

    private final InboxReputationDetail.View viewListener;
    ReputationAdapter.ReputationListener reputationListener;

    public InboxReputationDetailTypeFactoryImpl(InboxReputationDetailFragment inboxReputationDetail) {
        this.viewListener = inboxReputationDetail;
        this.reputationListener = (ReputationAdapter.ReputationListener) inboxReputationDetail;
    }

    @Override
    public int type(InboxReputationDetailHeaderViewModel model) {
        return InboxReputationDetailHeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(InboxReputationDetailItemViewModel model) {
        return InboxReputationDetailItemViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;

        if (type == InboxReputationDetailHeaderViewHolder.LAYOUT)
            viewHolder = new InboxReputationDetailHeaderViewHolder(view,
                    reputationListener);
        else if (type == InboxReputationDetailItemViewHolder.LAYOUT)
            viewHolder = new InboxReputationDetailItemViewHolder(view, viewListener);
        else
            viewHolder = super.createViewHolder(view, type);
        return viewHolder;
    }
}
