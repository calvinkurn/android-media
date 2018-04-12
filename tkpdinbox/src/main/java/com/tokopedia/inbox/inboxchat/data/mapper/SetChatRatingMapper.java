package com.tokopedia.inbox.inboxchat.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.inbox.inboxchat.data.pojo.SetChatRatingPojo;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by alvinatin on 28/03/18.
 */

public class SetChatRatingMapper implements
        Func1<Response<SetChatRatingPojo>, SetChatRatingPojo> {

    @Inject
    public SetChatRatingMapper(){}

    @Override
    public SetChatRatingPojo call(Response<SetChatRatingPojo> setChatRatingPojoResponse) {
        return getDataorError(setChatRatingPojoResponse);
    }

    private SetChatRatingPojo getDataorError(Response<SetChatRatingPojo> response) {
        if (response.isSuccessful()) {
            if (response.body() != null) {
                if (response.body().getStatus().equals("OK")) {
                    return response.body();
                } else {
                    throw new ErrorMessageException(response.body().getErrorDetails(),
                            response.body().getCode());
                }
            } else {
                if(response.errorBody() != null) {
                    try {
                        throw new ErrorMessageException(response.errorBody().string(),
                                response.code());
                    }
                    catch (IOException ioex){
                        throw new ErrorMessageException(response.errorBody().toString(),response
                                .code());
                    }
                }
                else {
                    throw new ErrorMessageException("Error when retrieving response",
                            response.code());
                }
            }
        } else {
            throw new ErrorMessageException(response.message(),
                    response.code());
        }
    }
}
