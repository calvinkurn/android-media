package com.tokopedia.flight.common.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;

import java.io.IOException;
import java.util.List;

/**
 * Created by User on 11/28/2017.
 */

public class FlightErrorResponse extends BaseResponseError {
    private static final String ERROR_KEY = "errors";

    @SerializedName(ERROR_KEY)
    @Expose
    private List<FlightError> errorList;

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
        return new FlightException(errorList);
    }

}
