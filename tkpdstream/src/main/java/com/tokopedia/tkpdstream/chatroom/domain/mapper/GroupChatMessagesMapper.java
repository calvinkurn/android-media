package com.tokopedia.tkpdstream.chatroom.domain.mapper;

import com.sendbird.android.AdminMessage;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.UserMessage;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.AdminAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 2/7/18.
 */

public class GroupChatMessagesMapper {

    @Inject
    public GroupChatMessagesMapper() {
    }

    public List<Visitable> map(List<BaseMessage> list) {
        List<Visitable> listViewModel = new ArrayList<>();
        for (BaseMessage message : list) {
            if (mapMessage(message) != null)
                listViewModel.add(mapMessage(message));
        }
        return listViewModel;
    }

    private Visitable mapMessage(BaseMessage message) {
        if (message instanceof AdminMessage) {
            return mapToAdminMessage(message);
        } else if (message instanceof UserMessage) {
            return mapToUserMessage(message);
        } else {
            return null;
        }
    }

    private Visitable mapToUserMessage(BaseMessage message) {
        return new ChatViewModel(
                ((UserMessage) message).getMessage(),
                String.valueOf(message.getCreatedAt()),
                String.valueOf(message.getUpdatedAt()),
                String.valueOf(message.getMessageId()),
                ((UserMessage) message).getSender().getUserId(),
                ((UserMessage) message).getSender().getNickname(),
                ((UserMessage) message).getSender().getProfileUrl(),
                false,
                false
        );
    }

    private AdminAnnouncementViewModel mapToAdminMessage(BaseMessage message) {
        return new AdminAnnouncementViewModel(
                ((AdminMessage) message).getMessage(),
                String.valueOf(message.getCreatedAt()),
                String.valueOf(message.getUpdatedAt()),
                String.valueOf(message.getMessageId())
        );
    }

    public Visitable map(BaseMessage baseMessage) {
        return mapMessage(baseMessage);
    }
}
