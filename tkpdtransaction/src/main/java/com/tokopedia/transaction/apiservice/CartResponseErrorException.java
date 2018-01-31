package com.tokopedia.transaction.apiservice;

import java.io.IOException;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public class CartResponseErrorException extends IOException {

    private static final long serialVersionUID = -3672249531132491023L;

    private final CartHeaderResponse cartHeaderResponse;
    private final int errorCode;

    public CartResponseErrorException(int errorCode, CartHeaderResponse cartHeaderResponse) {
        this.cartHeaderResponse = cartHeaderResponse;
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return cartHeaderResponse.getMessageFormatted();
    }

}
