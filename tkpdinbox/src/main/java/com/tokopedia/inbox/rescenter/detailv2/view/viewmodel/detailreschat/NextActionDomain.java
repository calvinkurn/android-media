package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

/**
 * Created by yoasfs on 10/10/17.
 */

public class NextActionDomain {

    private String last;
    private NextActionDetailDomain detail;

    public NextActionDomain(String last, NextActionDetailDomain detail) {
        this.last = last;
        this.detail = detail;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public NextActionDetailDomain getDetail() {
        return detail;
    }

    public void setDetail(NextActionDetailDomain detail) {
        this.detail = detail;
    }
}
