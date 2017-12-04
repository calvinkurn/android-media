package com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class OrderDetailDomain {
    @Nullable
    private int id;

    @Nullable
    private int returnable;


    public OrderDetailDomain(@Nullable int id,
                             @Nullable int returnable) {
        this.id = id;
        this.returnable = returnable;
    }

    @Nullable
    public int getId() {
        return id;
    }

    public void setId(@Nullable int id) {
        this.id = id;
    }

    @Nullable
    public int getReturnable() {
        return returnable;
    }

    public void setReturnable(@Nullable int returnable) {
        this.returnable = returnable;
    }
}
