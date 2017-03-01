package com.tokopedia.seller.topads.exception;

import com.tokopedia.seller.topads.data.source.cloud.response.Error;

import java.util.List;

/**
 * @author normansyahputa on 2/13/17.
 */

public class ResponseErrorException extends RuntimeException {
    private List<Error> errorList;
    public ResponseErrorException(List<Error> errorList) {
        this.errorList = errorList;
    }

    public List<Error> getErrorList() {
        return errorList;
    }
}
