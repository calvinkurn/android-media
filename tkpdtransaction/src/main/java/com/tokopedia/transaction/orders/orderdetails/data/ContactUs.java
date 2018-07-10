package com.tokopedia.transaction.orders.orderdetails.data;

/**
 * Created by baghira on 11/05/18.
 */

public class ContactUs {
    private String helpText;
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
