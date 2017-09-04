package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.SendReviewValidateDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 8/31/17.
 */

public class SendReviewValidateMapper implements Func1<Response<TkpdResponse>,
        SendReviewValidateDomain> {

    @Override
    public SendReviewValidateDomain call(Response<TkpdResponse> response) {
        return null;
    }
}
