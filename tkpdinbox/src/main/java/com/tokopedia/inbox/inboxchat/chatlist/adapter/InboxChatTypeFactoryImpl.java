package com.tokopedia.inbox.inboxchat.chatlist.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.chatlist.viewmodel.EmptyChatModel;
import com.tokopedia.inbox.inboxchat.chatlist.fragment.InboxChatFragment;
import com.tokopedia.inbox.inboxchat.chatlist.listener.InboxChatContract;
import com.tokopedia.inbox.inboxchat.chatlist.presenter.InboxChatPresenter;
import com.tokopedia.inbox.inboxchat.chatlist.adapter.viewholder.chatlist.EmptyChatListViewHolder;
import com.tokopedia.inbox.inboxchat.chatlist.adapter.viewholder.chatlist.ListChatViewHolder;
import com.tokopedia.inbox.inboxchat.chatlist.adapter.viewholder.chatlist.TimeMachineListViewHolder;
import com.tokopedia.inbox.inboxchat.chatlist.viewmodel.ChatListViewModel;
import com.tokopedia.inbox.inboxchat.chatlist.viewmodel.TimeMachineListViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class InboxChatTypeFactoryImpl extends BaseAdapterTypeFactory implements InboxChatTypeFactory {

    InboxChatContract.View viewListener;
    InboxChatPresenter presenter;

    public InboxChatTypeFactoryImpl(InboxChatFragment context, InboxChatPresenter presenter) {
        this.viewListener = context;
        this.presenter = presenter;
    }


    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == ListChatViewHolder.LAYOUT)
            viewHolder = new ListChatViewHolder(view, viewListener, presenter);
        else if (type == TimeMachineListViewHolder.LAYOUT)
            viewHolder = new TimeMachineListViewHolder(view, viewListener);
        else if (type == EmptyChatListViewHolder.LAYOUT)
            viewHolder = new EmptyChatListViewHolder(view, view.getContext(), viewListener);
        else
            return super.createViewHolder(view, type);

        return viewHolder;
    }

    @Override
    public int type(ChatListViewModel chatListViewModel) {
        return ListChatViewHolder.LAYOUT;
    }

    @Override
    public int type(TimeMachineListViewModel timeMachineListViewModel) {
        return TimeMachineListViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyChatModel emptyChatModel) {
        return EmptyChatListViewHolder.LAYOUT;
    }
}
