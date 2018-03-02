package com.tokopedia.tkpdstream.chatroom.domain.mapper;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.sendbird.android.AdminMessage;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.android.UserMessage;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.domain.pojo.ActivePollPojo;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.AdminAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChatViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ImageViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.VoteAnnouncementViewModel;
import com.tokopedia.tkpdstream.common.util.TimeConverter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 2/7/18.
 */

public class GroupChatMessagesMapper {

    private static final String IMAGE = "image";

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
            return mapToAdminMessage((AdminMessage) message);
        } else if (message instanceof UserMessage) {
            return mapToUserMessage((UserMessage) message);
        } else if (message instanceof FileMessage
                && ((FileMessage) message).getType().toLowerCase().contains(IMAGE)
                && !TextUtils.isEmpty(((FileMessage) message).getUrl())) {
            return mapToImageMessage((FileMessage) message);
        } else {
            return null;
        }
    }

    private Visitable mapToImageMessage(FileMessage message) {
        return new ImageViewModel(
                message.getUrl(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                String.valueOf(message.getMessageId()),
                message.getSender().getUserId(),
                message.getSender().getNickname(),
                message.getSender().getProfileUrl(),
                false,
                false
        );
    }

    private Visitable mapToUserMessage(UserMessage message) {
        switch (message.getCustomType()) {
            case VoteAnnouncementViewModel.POLLING_START:
                return mapCustomData(message,
                        message.getData().replace("\\\"", "\""));
            default:
                return new ChatViewModel(
                        message.getMessage(),
                        message.getCreatedAt(),
                        message.getUpdatedAt(),
                        String.valueOf(message.getMessageId()),
                        message.getSender().getUserId(),
                        message.getSender().getNickname(),
                        message.getSender().getProfileUrl(),
                        false,
                        false
                );
        }
    }

    private VoteAnnouncementViewModel mapCustomData(UserMessage message, String json) {
        Gson gson = new Gson();
        ActivePollPojo pojo = gson.fromJson(json, ActivePollPojo.class);

        return new VoteAnnouncementViewModel(
                pojo.getDescription(),
                message.getCustomType(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                String.valueOf(message.getMessageId()),
                message.getSender().getUserId(),
                message.getSender().getNickname(),
                message.getSender().getProfileUrl(),
                false,
                true
        );
    }

    private AdminAnnouncementViewModel mapToAdminMessage(AdminMessage message) {
        return new AdminAnnouncementViewModel(
                message.getMessage(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                String.valueOf(message.getMessageId())
        );
    }

    public Visitable map(BaseMessage baseMessage) {
        return mapMessage(baseMessage);
    }
}
