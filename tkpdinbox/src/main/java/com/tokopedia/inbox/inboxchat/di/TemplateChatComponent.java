package com.tokopedia.inbox.inboxchat.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.inboxchat.fragment.ChatRoomFragment;
import com.tokopedia.inbox.inboxchat.fragment.EditTemplateChatFragment;
import com.tokopedia.inbox.inboxchat.fragment.InboxChatFragment;
import com.tokopedia.inbox.inboxchat.fragment.SendChatFragment;
import com.tokopedia.inbox.inboxchat.fragment.TemplateChatFragment;

import dagger.Component;

/**
 * Created by stevenfredian on 9/14/17.
 */

@TemplateChatScope
@Component(modules = TemplateChatModule.class, dependencies = AppComponent.class)
public interface TemplateChatComponent {
    void inject(TemplateChatFragment fragment);

    void inject(EditTemplateChatFragment fragment);
}
