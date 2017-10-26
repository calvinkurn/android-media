package com.tokopedia.inbox.inboxchat.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatListViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public interface InboxChatTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(ChatListViewModel chatListViewModel);
}
