package com.tokopedia.tkpdstream.chatroom.domain.usecase;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.User;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.domain.mapper.GroupChatMessagesMapper;
import com.tokopedia.tkpdstream.chatroom.domain.mapper.UserActionMapper;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.UserActionViewModel;

import javax.inject.Inject;

/**
 * @author by nisie on 2/19/18.
 */

public class ChannelHandlerUseCase {

    private GroupChatMessagesMapper mapper;
    private UserActionMapper userMapper;

    @Inject
    public ChannelHandlerUseCase(GroupChatMessagesMapper mapper,
                                 UserActionMapper userMapper) {
        this.mapper = mapper;
        this.userMapper = userMapper;
    }

    public interface ChannelHandlerListener {
        void onMessageReceived(Visitable map);

        void onMessageDeleted(long msgId);

        void onMessageUpdated(Visitable map);

        void onUserEntered(UserActionViewModel userActionViewModel, String participantCount);

        void onUserExited(UserActionViewModel userActionViewModel, String participantCount);

        void onUserBanned();

        void onChannelDeleted();

        void onChannelFrozen();
    }

    public void execute(final String mChannelUrl, String channelHandlerId, final ChannelHandlerListener listener) {
        SendBird.addChannelHandler(channelHandlerId, new SendBird.ChannelHandler() {

            @Override
            public void onChannelFrozen(OpenChannel channel) {
                super.onChannelFrozen(channel);
                listener.onChannelFrozen();
            }

            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                try {
                    if (baseChannel.getUrl().equals(mChannelUrl)) {
                        listener.onMessageReceived(mapper.map(baseMessage));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onUserBanned(OpenChannel channel, User user) {
                super.onUserBanned(channel, user);
                listener.onUserBanned();
            }

            @Override
            public void onChannelDeleted(String channelUrl, BaseChannel.ChannelType channelType) {
                super.onChannelDeleted(channelUrl, channelType);
                listener.onChannelDeleted();
            }

            @Override
            public void onMessageDeleted(BaseChannel baseChannel, long msgId) {
                super.onMessageDeleted(baseChannel, msgId);
                if (baseChannel.getUrl().equals(mChannelUrl)) {
                    listener.onMessageDeleted(msgId);
                }
            }

            @Override
            public void onMessageUpdated(BaseChannel channel, BaseMessage message) {
                super.onMessageUpdated(channel, message);
                if (channel.getUrl().equals(mChannelUrl)) {
                    listener.onMessageUpdated(mapper.map(message));
                }
            }

            @Override
            public void onUserEntered(OpenChannel channel, User user) {
                if (channel.getUrl().equals(mChannelUrl)) {
                    listener.onUserEntered(userMapper.mapUserEnter(user), String.valueOf(channel
                            .getParticipantCount()));
                }
            }

            @Override
            public void onUserExited(OpenChannel channel, User user) {
                if (channel.getUrl().equals(mChannelUrl)) {
                    listener.onUserExited(userMapper.mapUserExit(user), String.valueOf(channel
                            .getParticipantCount()));
                }
            }
        });
    }
}
