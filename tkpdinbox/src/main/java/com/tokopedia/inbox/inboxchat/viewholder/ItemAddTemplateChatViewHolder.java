package com.tokopedia.inbox.inboxchat.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.presenter.TemplateChatContract;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;

/**
 * Created by stevenfredian on 11/29/17.
 */

public class ItemAddTemplateChatViewHolder extends AbstractViewHolder<TemplateChatModel>{

    @LayoutRes
    public static final int LAYOUT = R.layout.item_add_template_chat_settings;
    TemplateChatContract.View viewListener;
    View view;

    public ItemAddTemplateChatViewHolder(View itemView, TemplateChatContract.View viewListener) {
        super(itemView);
        view = itemView;
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final TemplateChatModel element) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onEnter(null);
            }
        });
    }
}
