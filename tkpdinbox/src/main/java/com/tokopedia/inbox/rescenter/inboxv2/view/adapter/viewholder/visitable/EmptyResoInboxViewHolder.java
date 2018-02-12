package com.tokopedia.inbox.rescenter.inboxv2.view.adapter.viewholder.visitable;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;

/**
 * Created by yfsx on 01/02/18.
 */

public class EmptyResoInboxViewHolder extends AbstractViewHolder<EmptyModel> {
    ResoInboxFragmentListener.View mainView;

    public EmptyResoInboxViewHolder(View itemView, ResoInboxFragmentListener.View mainView) {
        super(itemView);
        this.mainView = mainView;
    }

    @Override
    public void bind(EmptyModel element) {
    }

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_inbox_empty;
}
