package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.GetLikeDislikePojo;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 9/29/17.
 */

public class GetLikeDislikeMapper implements Func1<Response<TkpdResponse>,
        GetLikeDislikeReviewDomain> {
    @Override
    public GetLikeDislikeReviewDomain call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (!response.body().isNullData()) {
                GetLikeDislikePojo data = response.body().convertDataObj(
                        GetLikeDislikePojo.class);
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

    private GetLikeDislikeReviewDomain mappingToDomain(GetLikeDislikePojo data) {
        return new GetLikeDislikeReviewDomain();
    }
}
