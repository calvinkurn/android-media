package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.inboxdetail;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailItemViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailItemViewHolder extends
        AbstractViewHolder<InboxReputationDetailItemViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.inbox_reputation_detail_item;

    public InboxReputationDetailItemViewHolder(View itemView,
                                               InboxReputationDetail.View viewListener) {
        super(itemView);
    }

    @Override
    public void bind(InboxReputationDetailItemViewModel element) {

    }
}
