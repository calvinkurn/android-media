package com.tokopedia.tokocash.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.tokocash.network.exception.TokoCashException;

/**
 * Created by nabillasabbaha on 1/17/18.
 */

public class TokoCashErrorResponse extends BaseResponseError {

    @SerializedName("error")
    @Expose
    private String errorMessage;
    @SerializedName("error_description")
    @Expose
    private String errorDesc;
    @SerializedName("message_error")
    @Expose
    private String messageError;

    @Override
    public String getErrorKey() {
        return errorMessage;
    }

    @Override
    public boolean hasBody() {
        return errorMessage != null;
    }

    @Override
    public RuntimeException createException() {
        if (errorMessage != null)
            return new TokoCashException(errorMessage);
        else
            return new TokoCashException(messageError);
    }

    @Override
    public boolean hasCustomAdditionalError() {
        return false;
    }
}
