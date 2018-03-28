package com.tokopedia.inbox.inboxchat.domain.usecase;

import com.tokopedia.inbox.inboxchat.data.mapper.SetChatRatingMapper;
import com.tokopedia.inbox.inboxchat.data.network.ChatRatingApi;
import com.tokopedia.inbox.inboxchat.data.pojo.SetChatRatingPojo;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by alvinatin on 26/03/18.
 */

public class SetChatRatingUseCase extends UseCase<SetChatRatingPojo>{

    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_MESSAGE_ID = "message_id";
    public static final String PARAM_TIMESTAMP = "timestamp";
    public static final String PARAM_RATING = "rating";

    private final ChatRatingApi chatRatingApi;
    private final SetChatRatingMapper setChatRatingMapper;

    @Inject
    public SetChatRatingUseCase(ChatRatingApi chatRatingApi,
                                SetChatRatingMapper setChatRatingMapper){
        this.chatRatingApi = chatRatingApi;
        this.setChatRatingMapper = setChatRatingMapper;
    }

    @Override
    public Observable<SetChatRatingPojo> createObservable(RequestParams requestParams) {
        return chatRatingApi.setChatRating(requestParams).map(setChatRatingMapper);
    }

    public static RequestParams getParams(int messageId,
                                          int userId,
                                          String timeStamp,
                                          int rating){
        RequestParams param = RequestParams.create();
        param.putInt(PARAM_MESSAGE_ID, messageId);
        param.putInt(PARAM_USER_ID, userId);
        param.putString(PARAM_TIMESTAMP, timeStamp);
        param.putInt(PARAM_RATING, rating);
        return param;
    }
}
