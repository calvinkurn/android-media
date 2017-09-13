package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.EmptySearchModel;

/**
 * @author by nisie on 9/13/17.
 */

public class EmptyReputationSearchViewHolder extends AbstractViewHolder<EmptySearchModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_empty_search_reputation;

    TextView seeAllButton;

    public EmptyReputationSearchViewHolder(View itemView, final InboxReputation.View viewListener) {
        super(itemView);
        seeAllButton = (TextView) itemView.findViewById(R.id.see_all);
        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onResetSearch();
            }
        });
    }

    @Override
    public void bind(EmptySearchModel element) {

    }
}