package com.tokopedia.ride.history.domain.model;

/**
 * Created by alvarisi on 4/19/17.
 */

public class Payment {
    private String currency;
    private String value;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
