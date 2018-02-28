package com.tokopedia.tkpdstream.chatroom.domain.usecase;

import android.content.Context;

import com.sendbird.android.BaseMessage;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBirdException;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.domain.mapper.GroupChatMessagesMapper;
import com.tokopedia.tkpdstream.common.util.GroupChatErrorHandler;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 2/21/18.
 */

public class LoadPreviousChatMessagesUseCase {

    public static final int PARAM_LIMIT_MESSAGE = 30;
    public static final boolean PARAM_IS_REVERSE = true;

    GroupChatMessagesMapper mapper;

    public interface LoadPreviousChatMessagesListener {
        void onGetPreviousMessages(List<Visitable> map);

        void onErrorGetPreviousMessages(String errorMessage);
    }

    @Inject
    public LoadPreviousChatMessagesUseCase(GroupChatMessagesMapper mapper) {
        this.mapper = mapper;
    }


    public void execute(final Context context,
                        PreviousMessageListQuery previousMessageListQuery,
                        final LoadPreviousChatMessagesListener listener) {
        previousMessageListQuery.load(PARAM_LIMIT_MESSAGE, PARAM_IS_REVERSE, new PreviousMessageListQuery
                .MessageListQueryResult() {
            @Override
            public void onResult(List<BaseMessage> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();
                    listener.onErrorGetPreviousMessages(GroupChatErrorHandler.getSendBirdErrorMessage
                            (context, e, true));
                    return;
                }

                try {
                    listener.onGetPreviousMessages(mapper.map(list));
                } catch (NullPointerException npe) {
                    listener.onErrorGetPreviousMessages(GroupChatErrorHandler.getErrorMessage
                            (context, npe, true));
                }
            }
        });
    }
}
