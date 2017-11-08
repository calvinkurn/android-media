package com.tokopedia.flight.search.view.model.filter;

import com.tokopedia.flight.R;

/**
 * Created by User on 11/1/2017.
 */

public enum TransitEnum {
    DIRECT(1,R.string.direct),
    ONE(2,R.string.one_trasit),
    TWO(3,R.string.two_transit),
    THREE_OR_MORE(4,R.string.more_than_2_transit);

    private int id;
    private int valueRes;
    private TransitEnum(int id, int valueRes) {
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