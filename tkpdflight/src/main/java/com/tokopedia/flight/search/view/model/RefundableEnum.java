package com.tokopedia.flight.search.view.model;

import com.tokopedia.flight.R;

/**
 * Created by User on 11/1/2017.
 */

public enum RefundableEnum {

    REFUNDABLE(1,R.string.refundable),
    NOT_REFUNDABLE(2,R.string.non_refundable);

    private int id;
    private int valueRes;
    private RefundableEnum(int id, int valueRes) {
        this.id = id;
        this.valueRes = valueRes;
    }

    public int getId() {
        return id;
    }

    public int getValueRes() {
        return valueRes;
    }

}