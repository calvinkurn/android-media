package com.tokopedia.loyalty.exception;

import com.tokopedia.loyalty.domain.entity.response.TokoPointHeaderResponse;

import java.io.IOException;

/**
 * @author anggaprasetiyo on 13/12/17.
 */

public class TokoPointResponseErrorException extends IOException {

    private static final long serialVersionUID = -5802851155176779412L;
    private final TokoPointHeaderResponse tokoPointHeaderResponse;
    private final int errorCode;

    public TokoPointResponseErrorException(int errorCode, TokoPointHeaderResponse tokoPointHeaderResponse) {
        this.tokoPointHeaderResponse = tokoPointHeaderResponse;
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return tokoPointHeaderResponse.getMessageFormatted();
    }
}
