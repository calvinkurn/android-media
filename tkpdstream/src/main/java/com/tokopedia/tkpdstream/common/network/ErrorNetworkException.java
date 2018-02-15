package com.tokopedia.tkpdstream.common.network;

import java.io.IOException;
import java.util.List;

/**
 * @author by nisie on 2/3/18.
 */

public class ErrorNetworkException extends IOException {

    private List<NetworkErrorPojo> errorList;

    public List<NetworkErrorPojo> getErrorList() {
        return errorList;
    }

    public ErrorNetworkException(List<NetworkErrorPojo> errorList) {
        this.errorList = errorList;
    }
}
