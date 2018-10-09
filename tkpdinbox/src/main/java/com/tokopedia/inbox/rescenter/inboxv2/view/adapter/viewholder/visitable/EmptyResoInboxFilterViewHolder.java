package com.tokopedia.inbox.rescenter.inboxv2.view.adapter.viewholder.visitable;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.EmptyInboxFilterDataModel;

/**
 * Created by yfsx on 01/02/18.
 */

public class EmptyResoInboxFilterViewHolder extends AbstractViewHolder<EmptyInboxFilterDataModel> {

    ResoInboxFragmentListener.View mainView;
    private Button btnResetFilter;

    public EmptyResoInboxFilterViewHolder(View itemView, ResoInboxFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
        btnResetFilter = (Button) itemView.findViewById(R.id.btn_reset_filter);

    }

    @Override
    public void bind(EmptyInboxFilterDataModel element) {
        btnResetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.onResetFilterButtonClicked();
            }
        });
    }

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_inbox_filter_empty;
}
