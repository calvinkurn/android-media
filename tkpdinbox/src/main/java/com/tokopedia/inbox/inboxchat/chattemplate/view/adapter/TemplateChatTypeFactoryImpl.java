package com.tokopedia.inbox.inboxchat.chattemplate.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.chatroom.fragment.ChatRoomFragment;
import com.tokopedia.inbox.inboxchat.chatroom.listener.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.chattemplate.view.adapter.viewholder.TemplateChatViewHolder;
import com.tokopedia.inbox.inboxchat.chattemplate.view.viewmodel.TemplateChatModel;

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
