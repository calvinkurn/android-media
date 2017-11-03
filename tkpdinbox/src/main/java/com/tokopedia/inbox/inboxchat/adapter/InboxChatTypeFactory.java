package com.tokopedia.inbox.inboxchat.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.EmptyChatModel;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatListViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TimeMachineListViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public interface InboxChatTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(ChatListViewModel chatListViewModel);

    int type(TimeMachineListViewModel timeMachineListViewModel);

    int type(EmptyChatModel emptyChatModel);
}
