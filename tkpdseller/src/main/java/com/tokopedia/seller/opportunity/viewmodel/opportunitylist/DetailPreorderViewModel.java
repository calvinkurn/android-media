package com.tokopedia.seller.opportunity.viewmodel.opportunitylist;

/**
 * Created by nisie on 3/21/17.
 */

public class DetailPreorderViewModel {
    private int preorderStatus;
    private String preorderProcessTimeType;
    private String preorderProcessTimeTypeString;
    private String preorderProcessTime;

    public int getPreorderStatus() {
        return preorderStatus;
    }

    public void setPreorderStatus(int preorderStatus) {
        this.preorderStatus = preorderStatus;
    }

    public String getPreorderProcessTimeType() {
        return preorderProcessTimeType;
    }

    public void setPreorderProcessTimeType(String preorderProcessTimeType) {
        this.preorderProcessTimeType = preorderProcessTimeType;
    }

    public String getPreorderProcessTimeTypeString() {
        return preorderProcessTimeTypeString;
    }

    public void setPreorderProcessTimeTypeString(String preorderProcessTimeTypeString) {
        this.preorderProcessTimeTypeString = preorderProcessTimeTypeString;
    }

    public String getPreorderProcessTime() {
        return preorderProcessTime;
    }

    public void setPreorderProcessTime(String preorderProcessTime) {
        this.preorderProcessTime = preorderProcessTime;
    }
}
