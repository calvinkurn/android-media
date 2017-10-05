package com.tokopedia.transaction.cart.interactor.exception;

/**
 * Created by kris on 4/17/17. Tokopedia
 */

public class WrongEditCartException extends RuntimeException {
    public WrongEditCartException(String errorMessage) {
        super(errorMessage);
    }
}
