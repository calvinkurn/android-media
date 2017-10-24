package com.tokopedia.otp.securityquestion.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.securityquestion.domain.pojo.SecurityQuestionPojo;
import com.tokopedia.otp.securityquestion.data.model.securityquestion.QuestionViewModel;
import com.tokopedia.session.R;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/19/17.
 */

public class SecurityQuestionMapper implements Func1<Response<TkpdResponse>, QuestionViewModel> {
    @Override
    public QuestionViewModel call(Response<TkpdResponse> response) {
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

    private QuestionViewModel mappingToViewModel(SecurityQuestionPojo pojo) {
        return new QuestionViewModel(pojo.getQuestion(),
                pojo.getTitle());
    }
}
