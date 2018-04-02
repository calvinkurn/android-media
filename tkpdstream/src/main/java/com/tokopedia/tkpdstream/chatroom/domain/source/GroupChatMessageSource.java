package com.tokopedia.tkpdstream.chatroom.domain.source;

import android.content.Context;

import com.sendbird.android.BaseMessage;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBirdException;
import com.tokopedia.tkpdstream.chatroom.domain.mapper.GroupChatMessagesMapper;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.GetGroupChatMessagesFirstTimeUseCase;
import com.tokopedia.tkpdstream.common.util.GroupChatErrorHandler;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.tkpdstream.chatroom.domain.usecase.GetGroupChatMessagesFirstTimeUseCase.PARAM_IS_REVERSE;
import static com.tokopedia.tkpdstream.chatroom.domain.usecase.GetGroupChatMessagesFirstTimeUseCase.PARAM_LIMIT_MESSAGE;

/**
 * @author by nisie on 2/7/18.
 */

public class GroupChatMessageSource {

    @Inject
    public GroupChatMessageSource(GroupChatMessagesMapper mapper) {
        this.mapper = mapper;
    }

    private GroupChatMessagesMapper mapper;

    public void getMessagesFirstTime(final Context context,
                                     String channelUrl,
                                     OpenChannel mChannel,
                                     final GetGroupChatMessagesFirstTimeUseCase.GetGroupChatMessagesFirstTimeListener listener) {

        final PreviousMessageListQuery previousMessageListQuery = mChannel
                .createPreviousMessageListQuery();
        previousMessageListQuery.load(PARAM_LIMIT_MESSAGE, PARAM_IS_REVERSE, new PreviousMessageListQuery.MessageListQueryResult() {
            @Override
            public void onResult(List<BaseMessage> list, SendBirdException e) {
                if (e != null) {
                    listener.onErrorGetMessagesFirstTime(GroupChatErrorHandler.getSendBirdErrorMessage
                            (context, e, false));
                } else {
                    try {
                        listener.onGetMessagesFirstTime(mapper.map(list), previousMessageListQuery);
                    } catch (NullPointerException npe) {
                        listener.onErrorGetMessagesFirstTime(
                                GroupChatErrorHandler.getErrorMessage(context, npe, false));
                    }
                }

            }
        });

    }
}
