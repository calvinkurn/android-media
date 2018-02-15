package com.tokopedia.tkpdstream.channel.di;

import com.tokopedia.tkpdstream.channel.view.fragment.ChannelFragment;
import com.tokopedia.tkpdstream.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;

import dagger.Component;

/**
 * @author by nisie on 2/3/18.
 */
@ChannelScope
@Component(modules = ChannelModule.class, dependencies = StreamComponent.class)
public interface ChannelComponent {

    void inject(ChannelFragment fragment);

}
