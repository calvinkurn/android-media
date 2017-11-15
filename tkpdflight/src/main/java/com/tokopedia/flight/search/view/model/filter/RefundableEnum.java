package com.tokopedia.flight.search.view.model.filter;

import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.R;

/**
 * Created by User on 11/1/2017.
 */

public enum RefundableEnum implements ItemType {

    REFUNDABLE(1,R.string.refundable),
    PARTIAL_REFUNDABLE(2,R.string.partial_refundable),
    NOT_REFUNDABLE(3,R.string.non_refundable);

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

    @Override
    public int getType() {
        return 0;
    }
}