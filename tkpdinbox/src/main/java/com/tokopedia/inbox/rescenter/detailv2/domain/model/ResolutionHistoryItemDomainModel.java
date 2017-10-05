package com.tokopedia.inbox.rescenter.detailv2.domain.model;

/**
 * Created by hangnadi on 3/17/17.
 */

public class ResolutionHistoryItemDomainModel {

    private String date;
    private String remark;
    private String actionBy;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }
}
