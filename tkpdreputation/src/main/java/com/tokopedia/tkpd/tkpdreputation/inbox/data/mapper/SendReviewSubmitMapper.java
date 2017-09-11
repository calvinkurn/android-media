package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.SendReviewSubmitPojo;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewSubmitDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/5/17.
 */

public class SendReviewSubmitMapper implements Func1<Response<TkpdResponse>, SendReviewSubmitDomain> {
    @Override
    public SendReviewSubmitDomain call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals("")) {
                SendReviewSubmitPojo data = response.body().convertDataObj(SendReviewSubmitPojo
                        .class);
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

    private SendReviewSubmitDomain mappingToDomain(SendReviewSubmitPojo data) {
        return new SendReviewSubmitDomain(
                data.getReviewId(),
                data.getIsSuccess()
        );
    }
}
