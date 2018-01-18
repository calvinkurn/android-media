package com.tokopedia.otp.cotp.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.otp.cotp.domain.pojo.ListMethodItemPojo;
import com.tokopedia.otp.cotp.view.viewmodel.ListVerificationMethod;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 1/18/18.
 */

public class VerificationMethodMapper implements Func1<Response<TkpdResponse>, ListVerificationMethod> {

    @Inject
    public VerificationMethodMapper() {
    }

    @Override
    public ListVerificationMethod call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                ListMethodItemPojo pojo = response.body().convertDataObj(ListMethodItemPojo.class);
                return convertToDomain(pojo);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
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

    private ListVerificationMethod convertToDomain(ListMethodItemPojo pojo) {
        ArrayList<MethodItem> list = new ArrayList<>();
        return new ListVerificationMethod(list);
    }
}
