package com.tokopedia.inbox.inboxchat.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.data.pojo.SendMessagePojo;
import com.tokopedia.inbox.inboxchat.viewmodel.SendMessageViewModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/25/17.
 */

public class SendMessageMapper implements Func1<Response<TkpdResponse>, SendMessageViewModel> {
    @Override
    public SendMessageViewModel call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || !response.body().isNullData() && response.body().getErrorMessages() == null) {
                SendMessagePojo data = response.body().convertDataObj(SendMessagePojo.class);
                return mappingToDomain(data);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(MainApplication.getAppContext().getString
                            (R.string.default_request_error_unknown));
                }
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private SendMessageViewModel mappingToDomain(SendMessagePojo data) {
        return new SendMessageViewModel(data.isSuccess());
    }
}
