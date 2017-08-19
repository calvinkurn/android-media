package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;

/**
 * @author by nisie on 8/19/17.
 */

public class EmptyReputationViewHolder extends AbstractViewHolder<EmptyModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.list_empty_reputation;

    public EmptyReputationViewHolder(View itemView, InboxReputation.View viewListener) {
        super(itemView);
    }

    @Override
    public void bind(EmptyModel element) {

    }
}
