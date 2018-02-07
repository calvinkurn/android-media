package com.tokopedia.tkpdstream.channel.domain.usecase;

import com.sendbird.android.SendBirdException;
import com.tokopedia.tkpdstream.channel.domain.source.GroupChatMessageSource;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GetGroupChatMessagesFirstTimeUseCase {

    public static final int PARAM_LIMIT_MESSAGE = 30;
    public static final boolean PARAM_IS_REVERSE = true;

    private GroupChatMessageSource source;

    public interface SendbirdChannelListener {
        void onGetMessages();

        void onErrorGetMessagesFirstTime(SendBirdException e);

    }

    @Inject
    public GetGroupChatMessagesFirstTimeUseCase(GroupChatMessageSource source) {
        this.source = source;
    }


    public void execute(String channelUrl,
                        final GetGroupChatMessagesFirstTimeUseCase.SendbirdChannelListener listener) {
        source.getMessagesFirstTime(channelUrl, listener);
    }


}