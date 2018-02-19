package com.tokopedia.tkpdstream.chatroom.domain.usecase;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.SendBird;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.domain.ConnectionManager;
import com.tokopedia.tkpdstream.chatroom.domain.mapper.GroupChatMessagesMapper;

import javax.inject.Inject;

/**
 * @author by nisie on 2/19/18.
 */

public class ChannelHandlerUseCase {

    private GroupChatMessagesMapper mapper;

    @Inject
    public ChannelHandlerUseCase(GroupChatMessagesMapper mapper) {
        this.mapper = mapper;
    }

    public interface ChannelHandlerListener {
        void onMessageReceived(Visitable map);

        void onMessageDeleted(long msgId);

        void onMessageUpdated(Visitable map);

    }

    public void execute(final String mChannelUrl, final ChannelHandlerListener listener) {
        SendBird.addChannelHandler(ConnectionManager.CHANNEL_HANDLER_ID, new SendBird
                .ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                if (baseChannel.getUrl().equals(mChannelUrl)) {
                    listener.onMessageReceived(mapper.map(baseMessage));
                }
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
        });
    }
}
