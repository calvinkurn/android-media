package com.tokopedia.loyalty.domain.exception;

/**
 * @author anggaprasetiyo on 07/12/17.
 */

public class TokoPointDBServiceException extends RuntimeException {

    private static final long serialVersionUID = -5737718459892415751L;

    public TokoPointDBServiceException(String message) {
        super(message);
    }
}
