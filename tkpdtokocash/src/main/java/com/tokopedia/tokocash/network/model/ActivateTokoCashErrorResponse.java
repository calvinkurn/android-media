package com.tokopedia.tokocash.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.tokocash.network.exception.TokoCashException;

import java.util.List;

/**
 * Created by nabillasabbaha on 1/17/18.
 */

public class ActivateTokoCashErrorResponse extends BaseResponseError {

    private static final String ERROR = "message_error";

    @SerializedName(ERROR)
    @Expose
    private List<String> messageError;

    @Override
    public String getErrorKey() {
        return ERROR;
    }

    @Override
    public boolean hasBody() {
        return messageError != null;
    }

    @Override
    public RuntimeException createException() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < messageError.size(); i++) {
            stringBuilder.append(messageError.get(i));
            if (i < messageError.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return new TokoCashException(stringBuilder.toString().trim());
    }

    @Override
    public boolean hasCustomAdditionalError() {
        return false;
    }
}
