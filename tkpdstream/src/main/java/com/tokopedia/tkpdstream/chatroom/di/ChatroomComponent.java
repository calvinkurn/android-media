package com.tokopedia.tkpdstream.chatroom.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.tkpdstream.chatroom.view.activity.GroupChatActivity;
import com.tokopedia.tkpdstream.chatroom.view.fragment.ChannelInfoFragment;
import com.tokopedia.tkpdstream.chatroom.view.fragment.ChannelVoteFragment;
import com.tokopedia.tkpdstream.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;

import dagger.Component;

/**
 * @author by nisie on 2/15/18.
 */

@ChatroomScope
@Component(modules = ChatroomModule.class, dependencies = StreamComponent.class)
public interface ChatroomComponent {
    @ApplicationContext Context getApplicationContext();

    void inject(GroupChatFragment fragment);

    void inject(ChannelVoteFragment fragment);

    void inject(ChannelInfoFragment fragment);

    void inject(GroupChatActivity activity);
}
