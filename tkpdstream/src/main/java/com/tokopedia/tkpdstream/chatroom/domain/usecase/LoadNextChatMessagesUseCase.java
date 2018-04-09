package com.tokopedia.tkpdstream.chatroom.domain.usecase;

import android.content.Context;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBirdException;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.domain.mapper.GroupChatMessagesMapper;
import com.tokopedia.tkpdstream.common.util.GroupChatErrorHandler;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 2/26/18.
 */

public class LoadNextChatMessagesUseCase {
    private static final boolean PARAM_IS_INCLUSIVE = false;
    private static final int PARAM_LIMIT_NEXT_MESSAGE = 30;
    private static final boolean PARAM_IS_REVERSE = true;

    GroupChatMessagesMapper mapper;

    @Inject
    public LoadNextChatMessagesUseCase(GroupChatMessagesMapper mapper) {
        this.mapper = mapper;
    }

    public interface LoadNextChatMessagesListener {
        void onGetNextMessages(List<Visitable> map);

        void onErrorGetNextMessages(String errorMessage);
    }


    public void execute(final Context context, OpenChannel mChannel, long cursor, final
    LoadNextChatMessagesListener listener) {

        mChannel.getNextMessagesById(cursor, PARAM_IS_INCLUSIVE, PARAM_LIMIT_NEXT_MESSAGE,
                PARAM_IS_REVERSE, BaseChannel.MessageTypeFilter.ALL, "", new BaseChannel
                        .GetMessagesHandler() {
                    @Override
                    public void onResult(List<BaseMessage> list, SendBirdException e) {
                        if (e != null) {
                            // Error!
                            e.printStackTrace();
                            listener.onErrorGetNextMessages(GroupChatErrorHandler.getSendBirdErrorMessage
                                    (context, e, true));
                            return;
                        }

                        try {
                            listener.onGetNextMessages(mapper.map(list));
                        } catch (NullPointerException npe) {
                            listener.onErrorGetNextMessages(GroupChatErrorHandler.getErrorMessage
                                    (context, npe, true));
                        }
                    }
                });
    }
}
