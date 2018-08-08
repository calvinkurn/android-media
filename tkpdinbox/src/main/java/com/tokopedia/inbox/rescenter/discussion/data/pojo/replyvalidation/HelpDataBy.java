
package com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpDataBy {

    @SerializedName("by_seller")
    @Expose
    private int bySeller;
    @SerializedName("by_customer")
    @Expose
    private int byCustomer;
    @SerializedName("user_label")
    @Expose
    private String userLabel;
    @SerializedName("user_label_id")
    @Expose
    private int userLabelId;

    public int getBySeller() {
        return bySeller;
    }

    public void setBySeller(int bySeller) {
        this.bySeller = bySeller;
    }

    public int getByCustomer() {
        return byCustomer;
    }

    public void setByCustomer(int byCustomer) {
        this.byCustomer = byCustomer;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public int getUserLabelId() {
        return userLabelId;
    }

    public void setUserLabelId(int userLabelId) {
        this.userLabelId = userLabelId;
    }

}
