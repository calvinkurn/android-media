package com.tokopedia.tkpdstream.chatroom.di;

import com.tokopedia.tkpdstream.chatroom.view.activity.GroupChatActivity;
import com.tokopedia.tkpdstream.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;

import dagger.Component;

/**
 * @author by nisie on 2/15/18.
 */

@ChatroomScope
@Component(modules = ChatroomModule.class, dependencies = StreamComponent.class)
public interface ChatroomComponent {
    void inject(GroupChatFragment fragment);

    void inject(GroupChatActivity activity);
}
