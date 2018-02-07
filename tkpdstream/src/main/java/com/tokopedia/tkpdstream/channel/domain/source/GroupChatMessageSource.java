package com.tokopedia.tkpdstream.channel.domain.source;

import com.sendbird.android.BaseMessage;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBirdException;
import com.tokopedia.tkpdstream.channel.domain.mapper.GroupChatMessagesMapper;
import com.tokopedia.tkpdstream.channel.domain.usecase.GetGroupChatMessagesFirstTimeUseCase;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.tkpdstream.channel.domain.usecase.GetGroupChatMessagesFirstTimeUseCase.PARAM_IS_REVERSE;
import static com.tokopedia.tkpdstream.channel.domain.usecase.GetGroupChatMessagesFirstTimeUseCase.PARAM_LIMIT_MESSAGE;

/**
 * @author by nisie on 2/7/18.
 */

public class GroupChatMessageSource {

    @Inject
    public GroupChatMessageSource(GroupChatMessagesMapper mapper) {
        this.mapper = mapper;
    }

    GroupChatMessagesMapper mapper;


    public void getMessagesFirstTime(String channelUrl,
                                     final GetGroupChatMessagesFirstTimeUseCase.SendbirdChannelListener listener) {

        OpenChannel.getChannel(channelUrl, new OpenChannel.OpenChannelGetHandler() {
            @Override
            public void onResult(final OpenChannel openChannel, SendBirdException e) {
                if (e != null) {
                    listener.onErrorGetMessagesFirstTime(e);
                    return;
                }

                openChannel.enter(new OpenChannel.OpenChannelEnterHandler() {

                    @Override
                    public void onResult(SendBirdException e) {
                        if (e != null) {
                            listener.onErrorGetMessagesFirstTime(e);
                        }

                        PreviousMessageListQuery previousMessageListQuery = openChannel
                                .createPreviousMessageListQuery();
                        previousMessageListQuery.load(PARAM_LIMIT_MESSAGE, PARAM_IS_REVERSE, new PreviousMessageListQuery.MessageListQueryResult() {
                            @Override
                            public void onResult(List<BaseMessage> list, SendBirdException e) {
                                if (e != null) {
                                    listener.onErrorGetMessagesFirstTime(e);
                                } else {
                                    listener.onGetMessages();
                                }

                            }
                        });
                    }
                });
            }
        });

    }
}
