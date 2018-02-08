package com.tokopedia.inbox.inboxchat.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.inboxchat.fragment.ChatRoomFragment;

import dagger.Component;

/**
 * Created by stevenfredian on 9/14/17.
 */

@InboxChatScope
@Component(modules = ChatRoomModule.class, dependencies = AppComponent.class)
public interface ChatRoomComponent {
    void inject(ChatRoomFragment fragment);
}
