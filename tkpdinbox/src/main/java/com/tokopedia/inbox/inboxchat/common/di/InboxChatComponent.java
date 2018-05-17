package com.tokopedia.inbox.inboxchat.common.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.inboxchat.chatroom.data.network.ChatBotApi;
import com.tokopedia.inbox.inboxchat.chatroom.fragment.ChatRoomFragment;
import com.tokopedia.inbox.inboxchat.chatlist.fragment.InboxChatFragment;
import com.tokopedia.inbox.inboxchat.chatlist.fragment.SendChatFragment;

import dagger.Component;

/**
 * Created by stevenfredian on 9/14/17.
 */

@InboxChatScope
@Component(modules = InboxChatModule.class, dependencies = AppComponent.class)
public interface InboxChatComponent {
    void inject (InboxChatFragment fragment);

    void inject (ChatRoomFragment fragment);

    void inject (SendChatFragment fragment);

    ChatBotApi chatRatingApi();
}
