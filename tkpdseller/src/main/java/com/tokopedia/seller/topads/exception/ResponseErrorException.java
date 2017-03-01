package com.tokopedia.seller.topads.exception;

import com.tokopedia.seller.topads.data.source.cloud.response.Error;

import java.io.IOException;
import java.util.List;

/**
 * @author normansyahputa on 2/13/17.
 */

public class ResponseErrorException extends IOException {
    private List<Error> errorList;
    public ResponseErrorException(List<Error> errorList) {
        this.errorList = errorList;
    }

    public List<Error> getErrorList() {
        return errorList;
    }
}
