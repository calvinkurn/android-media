package com.tokopedia.transaction.checkout.domain;

/**
 * @author anggaprasetiyo on 08/02/18.
 */

public abstract class ConverterData<O, R> {

    public abstract R convert(O originData);
}
