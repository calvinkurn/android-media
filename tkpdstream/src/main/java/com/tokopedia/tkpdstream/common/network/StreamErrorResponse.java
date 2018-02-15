package com.tokopedia.tkpdstream.common.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;

import java.io.IOException;
import java.util.List;

/**
 * @author by nisie on 2/3/18.
 */

public class StreamErrorResponse extends BaseResponseError {
    private static final String ERROR_KEY = "errors";


    @SerializedName(ERROR_KEY)
    @Expose
    private List<NetworkErrorPojo> errorList;

    @Override
    public String getErrorKey() {
        return ERROR_KEY;
    }

    @Override
    public boolean hasBody() {
        return errorList!= null && errorList.size() > 0;
    }

    @Override
    public IOException createException() {
        return new ErrorNetworkException(errorList);
    }

}
