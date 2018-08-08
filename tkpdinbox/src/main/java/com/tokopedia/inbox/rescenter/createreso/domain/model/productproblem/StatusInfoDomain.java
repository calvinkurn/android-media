package com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class StatusInfoDomain {
    @Nullable
    private boolean show;
    @Nullable
    private String date;

    public StatusInfoDomain() {

    }

    public StatusInfoDomain(@Nullable boolean show,
                            @Nullable String date) {
        this.show = show;
        this.date = date;
    }

    @Nullable
    public boolean isShow() {
        return show;
    }

    public void setShow(@Nullable boolean show) {
        this.show = show;
    }

    @Nullable
    public String getDate() {
        return date;
    }

    public void setDate(@Nullable String date) {
        this.date = date;
    }
}
