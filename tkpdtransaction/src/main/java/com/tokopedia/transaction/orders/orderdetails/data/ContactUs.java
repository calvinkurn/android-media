package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by baghira on 11/05/18.
 */

public class ContactUs {
    @SerializedName("helpText")
    @Expose
    private String helpText;
    @SerializedName("helpUrl")
    @Expose
    private String helpUrl;

    public ContactUs(String helpText, String helpUrl) {
        this.helpText = helpText;
        this.helpUrl = helpUrl;
    }

    public String helpText(){
        return helpText;
    }

    public String helpUrl(){
        return helpUrl;
    }
    @Override
    public String toString() {
        return "[ContactUs:{"
                + "helpText="+helpText +" "
                + "helpUrl="+helpUrl
                + "}]";
    }
}
