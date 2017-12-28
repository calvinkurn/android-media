package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 24/08/17.
 */

public class FreeReturnDomain {

    @Nullable
    private String info;

    @Nullable
    private String link;

    public FreeReturnDomain(String info, String link) {
        this.info = info;
        this.link = link;
    }

    @Nullable
    public String getInfo() {
        return info;
    }

    public void setInfo(@Nullable String info) {
        this.info = info;
    }

    @Nullable
    public String getLink() {
        return link;
    }

    public void setLink(@Nullable String link) {
        this.link = link;
    }
}
