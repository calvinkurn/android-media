package com.tokopedia.inbox.rescenter.inboxv2.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.inboxv2.data.pojo.InboxSingleDataResponse;
import com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel.InboxItemViewModel;

import retrofit2.Response;
import rx.functions.Func1;

import static com.tokopedia.core.network.ErrorMessageException.DEFAULT_ERROR;

/**
 * Created by yfsx on 24/01/18.
 */

public class GetInboxSingleItemMapper implements Func1<Response<TkpdResponse>, InboxItemViewModel> {

    @Override
    public InboxItemViewModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private InboxItemViewModel mappingResponse(Response<TkpdResponse> response) {
        InboxSingleDataResponse dataResponse = response.body().convertDataObj(
                InboxSingleDataResponse.class);
        InboxItemViewModel model = GetInboxMapper.mappingItem(dataResponse.getInbox(), dataResponse.getActionBy());
        if (response.isSuccessful()) {
            if (response.raw().code() == ResponseStatus.SC_OK) {
                if (response.body().isNullData()) {
                    if (response.body().getErrorMessageJoined() != null || !response.body().getErrorMessageJoined().isEmpty()) {
                        throw new ErrorMessageException(response.body().getErrorMessageJoined());
                    } else {
                        throw new ErrorMessageException(DEFAULT_ERROR);
                    }
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }
}
