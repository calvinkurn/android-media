package com.tokopedia.tkpdstream.chatroom.domain.usecase;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBirdException;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.domain.source.GroupChatMessageSource;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GetGroupChatMessagesFirstTimeUseCase {

    public static final int PARAM_LIMIT_MESSAGE = 30;
    public static final boolean PARAM_IS_REVERSE = true;

    private GroupChatMessageSource source;

    public interface GetGroupChatMessagesListener {
        void onGetMessages(List<Visitable> map);

        void onErrorGetMessagesFirstTime(SendBirdException e);
    }

    @Inject
    public GetGroupChatMessagesFirstTimeUseCase(GroupChatMessageSource source) {
        this.source = source;
    }


    public void execute(String channelUrl,
                        OpenChannel mChannel,
                        final GetGroupChatMessagesListener listener) {
        source.getMessagesFirstTime(channelUrl,mChannel, listener);
    }


}