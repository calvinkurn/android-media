package com.tokopedia.inbox.inboxchat.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TypingChatModel;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.TimeMachineChatModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public interface ChatRoomTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(OppositeChatViewModel oppositeChatViewModel);

    int type(MyChatViewModel myChatViewModel);

    int type(TimeMachineChatModel timeMachineChatModel);

    int type(TypingChatModel typingChatModel);
}
