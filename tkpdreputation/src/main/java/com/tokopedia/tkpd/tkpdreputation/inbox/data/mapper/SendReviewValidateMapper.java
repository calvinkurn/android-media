package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.sendreview.SendReviewValidatePojo;
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
        if (response.isSuccessful()) {
            if (!response.body().isNullData()) {
                SendReviewValidatePojo data = response.body().convertDataObj(SendReviewValidatePojo.class);
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
            if (response.body() == null
                    || response.body().getErrorMessages() == null
                    || response.body().getErrorMessages().isEmpty()) {
                throw new RuntimeException(String.valueOf(response.code()));
            } else {
                throw new ErrorMessageException(response.body().getErrorMessageJoined());
            }
        }
    }

    private SendReviewValidateDomain mappingToDomain(SendReviewValidatePojo data) {
        return new SendReviewValidateDomain(
                data.getPostKey(),
                data.getReviewId(),
                data.getIsSuccess()
        );
    }
}
