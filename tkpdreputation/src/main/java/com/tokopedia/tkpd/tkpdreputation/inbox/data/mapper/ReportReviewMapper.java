package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.ReportReviewPojo;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.SkipReviewPojo;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.ReportReviewDomain;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/13/17.
 */

public class ReportReviewMapper implements Func1<Response<TkpdResponse>, ReportReviewDomain> {
    @Override
    public ReportReviewDomain call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (!response.body().isNullData()) {
                ReportReviewPojo data = response.body().convertDataObj(ReportReviewPojo.class);
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

    private ReportReviewDomain mappingToDomain(ReportReviewPojo data) {
        return null;
    }
}
