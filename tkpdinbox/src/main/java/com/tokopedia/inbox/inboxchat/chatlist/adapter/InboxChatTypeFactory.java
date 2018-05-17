package com.tokopedia.inbox.inboxchat.chatlist.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.chatlist.viewmodel.EmptyChatModel;
import com.tokopedia.inbox.inboxchat.chatlist.viewmodel.ChatListViewModel;
import com.tokopedia.inbox.inboxchat.chatlist.viewmodel.TimeMachineListViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public interface InboxChatTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(ChatListViewModel chatListViewModel);

    int type(TimeMachineListViewModel timeMachineListViewModel);

    int type(EmptyChatModel emptyChatModel);
}
