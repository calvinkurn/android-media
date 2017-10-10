package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ResolutionDomain {

    private int id;
    private int freeReturn;

    public ResolutionDomain(int id, int freeReturn) {
        this.id = id;
        this.freeReturn = freeReturn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(int freeReturn) {
        this.freeReturn = freeReturn;
    }
}
