
package com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class By {

    @SerializedName("by_seller")
    @Expose
    private Integer bySeller;
    @SerializedName("by_customer")
    @Expose
    private Integer byCustomer;
    @SerializedName("user_label_id")
    @Expose
    private Integer userLabelId;
    @SerializedName("user_label")
    @Expose
    private String userLabel;

    public Integer getBySeller() {
        return bySeller;
    }

    public void setBySeller(Integer bySeller) {
        this.bySeller = bySeller;
    }

    public Integer getByCustomer() {
        return byCustomer;
    }

    public void setByCustomer(Integer byCustomer) {
        this.byCustomer = byCustomer;
    }

    public Integer getUserLabelId() {
        return userLabelId;
    }

    public void setUserLabelId(Integer userLabelId) {
        this.userLabelId = userLabelId;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

}
