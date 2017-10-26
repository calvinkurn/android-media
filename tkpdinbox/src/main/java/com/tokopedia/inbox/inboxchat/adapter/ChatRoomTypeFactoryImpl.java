package com.tokopedia.inbox.inboxchat.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.fragment.ChatRoomFragment;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.viewholder.MyChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.OppositeChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewholder.TimeMachineChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.TimeMachineChatModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class ChatRoomTypeFactoryImpl extends BaseAdapterTypeFactory implements ChatRoomTypeFactory{

    ChatRoomContract.View viewListener;

    public ChatRoomTypeFactoryImpl(ChatRoomFragment context) {
        this.viewListener = context;
    }

    @Override
    public int type(OppositeChatViewModel oppositeChatViewModel) {
        return OppositeChatViewHolder.LAYOUT;
    }

    @Override
    public int type(MyChatViewModel myChatViewModel) {
        return MyChatViewHolder.LAYOUT;
    }

    @Override
    public int type(TimeMachineChatModel timeMachineChatModel) {
        return TimeMachineChatViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == OppositeChatViewHolder.LAYOUT)
            viewHolder = new OppositeChatViewHolder(view, viewListener);
        else if (type == MyChatViewHolder.LAYOUT)
            viewHolder = new MyChatViewHolder(view, viewListener);
        else if (type == TimeMachineChatViewHolder.LAYOUT)
            viewHolder = new TimeMachineChatViewHolder(view, viewListener);
        else
            return super.createViewHolder(view, type);

        return viewHolder;
    }
}
