package com.tokopedia.tkpdstream.chatroom.domain.usecase;

import android.content.Context;

import com.sendbird.android.BaseMessage;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBirdException;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdstream.chatroom.domain.mapper.GroupChatMessagesMapper;
import com.tokopedia.tkpdstream.common.util.GroupChatErrorHandler;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by nisie on 2/26/18.
 */

public class RefreshMessageUseCase {
    private static final int PARAM_LIMIT_MESSAGE = 30;
    private static final boolean PARAM_IS_REVERSE = true;

    public interface RefreshMessagesListener {
        void onSuccessRefreshMessage(List<Visitable> map, PreviousMessageListQuery
                previousMessageListQuery);

        void onErrorRefreshMessage(String errorMessage);
    }

    private GroupChatMessagesMapper mapper;

    @Inject
    public RefreshMessageUseCase(GroupChatMessagesMapper mapper) {
        this.mapper = mapper;

    }

    public void execute(final Context context, OpenChannel mChannel, final RefreshMessagesListener listener) {
        final PreviousMessageListQuery mPrevMessageListQuery = mChannel.createPreviousMessageListQuery();
        mPrevMessageListQuery.load(PARAM_LIMIT_MESSAGE, PARAM_IS_REVERSE, new PreviousMessageListQuery
                .MessageListQueryResult() {
            @Override
            public void onResult(List<BaseMessage> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();
                    listener.onErrorRefreshMessage(GroupChatErrorHandler.getSendBirdErrorMessage
                            (context, e, true));
                    return;
                }

                try {
                    listener.onSuccessRefreshMessage(mapper.map(list), mPrevMessageListQuery);
                } catch (NullPointerException npe) {
                    listener.onErrorRefreshMessage(GroupChatErrorHandler.getErrorMessage
                            (context, npe, true));
                }
            }
        });
    }
}
