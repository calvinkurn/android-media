package com.tokopedia.tkpdstream.chatroom.domain.mapper;

import com.sendbird.android.UserMessage;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;

import javax.inject.Inject;

/**
 * @author by nisie on 2/15/18.
 */

public class SendMessageMapper {

    @Inject
    public SendMessageMapper() {
    }

    public ChatViewModel map(UserMessage userMessage) {

        return new ChatViewModel(userMessage.getMessage(),
                String.valueOf(userMessage.getCreatedAt()),
                String.valueOf(userMessage.getUpdatedAt()),
                String.valueOf(userMessage.getMessageId()),
                userMessage.getSender().getUserId(),
                userMessage.getSender().getNickname(),
                userMessage.getSender().getProfileUrl(),
                false);
    }
}
