package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.inboxdetail;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailHeaderViewHolder extends
        AbstractViewHolder<InboxReputationDetailHeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.inbox_reputation_detail_header;

    public InboxReputationDetailHeaderViewHolder(View itemView,
                                                 InboxReputationDetail.View viewListener) {
        super(itemView);
    }

    @Override
    public void bind(InboxReputationDetailHeaderViewModel element) {

    }
}
