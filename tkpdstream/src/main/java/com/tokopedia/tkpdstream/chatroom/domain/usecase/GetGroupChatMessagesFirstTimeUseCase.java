package com.tokopedia.tkpdstream.chatroom.domain.usecase;

import android.content.Context;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
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

    public interface GetGroupChatMessagesFirstTimeListener {
        void onGetMessagesFirstTime(List<Visitable> map, PreviousMessageListQuery previousMessageListQuery);

        void onErrorGetMessagesFirstTime(String errorMessage);
    }

    @Inject
    public GetGroupChatMessagesFirstTimeUseCase(GroupChatMessageSource source) {
        this.source = source;
    }


    public void execute(Context context,
                        String channelUrl,
                        OpenChannel mChannel,
                        final GetGroupChatMessagesFirstTimeListener listener) {
        source.getMessagesFirstTime(context, channelUrl, mChannel, listener);
    }


}