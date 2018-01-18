package com.tokopedia.inbox.inboxchat.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.fragment.ChatRoomFragment;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewholder.TemplateChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class TemplateChatTypeFactoryImpl extends BaseAdapterTypeFactory implements TemplateChatTypeFactory{

    ChatRoomContract.View viewListener;

    public TemplateChatTypeFactoryImpl(ChatRoomFragment context) {
        this.viewListener = context;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == TemplateChatViewHolder.LAYOUT)
            viewHolder = new TemplateChatViewHolder(view, viewListener);
        else
            return super.createViewHolder(view, type);

        return viewHolder;
    }

    @Override
    public int type(TemplateChatModel templateChatModel) {
        return TemplateChatViewHolder.LAYOUT;
    }

}
