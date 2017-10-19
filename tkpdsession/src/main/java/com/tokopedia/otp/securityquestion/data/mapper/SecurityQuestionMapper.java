package com.tokopedia.otp.securityquestion.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.securityquestion.data.pojo.SecurityQuestionPojo;
import com.tokopedia.otp.securityquestion.domain.model.securityquestion.QuestionDomain;
import com.tokopedia.session.R;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/19/17.
 */

public class SecurityQuestionMapper implements Func1<Response<TkpdResponse>, QuestionDomain> {
    @Override
    public QuestionDomain call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                SecurityQuestionPojo pojo = response.body().convertDataObj(SecurityQuestionPojo
                        .class);
                return mappingToViewModel(pojo);
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

    private QuestionDomain mappingToViewModel(SecurityQuestionPojo pojo) {
        return new QuestionDomain(pojo.getQuestion(),
                pojo.getExample(),
                pojo.getTitle());
    }
}
