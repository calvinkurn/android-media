package com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class ProblemDomain {

    @Nullable
    private int type;

    @Nullable
    private String name;

    public ProblemDomain(@Nullable int type,
                         @Nullable String name) {
        this.type = type;
        this.name = name;
    }

    @Nullable
    public int getType() {
        return type;
    }

    public void setType(@Nullable int type) {
        this.type = type;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }
}
