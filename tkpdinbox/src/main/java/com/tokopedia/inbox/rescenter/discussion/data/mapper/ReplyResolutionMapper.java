package com.tokopedia.inbox.rescenter.detailv2.data.mapper;

import com.tkpd.library.utils.Logger;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.discussion.domain.model.NewReplyDiscussionModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 6/16/17.
 */

public class ReplyResolutionMapper implements Func1<Response<TkpdResponse>, NewReplyDiscussionModel> {

    public ReplyResolutionMapper() {
    }

    @Override
    public NewReplyDiscussionModel call(Response<TkpdResponse> response) {
        Logger.i("hangnadi", response.body().getStrResponse());
        throw new RuntimeException("hangnadi");
    }

}
