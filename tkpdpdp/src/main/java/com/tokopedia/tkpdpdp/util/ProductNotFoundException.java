package com.tokopedia.tkpdpdp.util;

public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String path) {
        super(path);
    }
}
