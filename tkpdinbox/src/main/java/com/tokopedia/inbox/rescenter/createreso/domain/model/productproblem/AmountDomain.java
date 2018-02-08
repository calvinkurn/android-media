package com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class AmountDomain {

    @Nullable
    private String idr;

    private int integer;

    public AmountDomain(@Nullable String idr,
                        int integer) {
        this.idr = idr;
        this.integer = integer;
    }


    @Nullable
    public String getIdr() {
        return idr;
    }

    public void setIdr(@Nullable String idr) {
        this.idr = idr;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }
}
