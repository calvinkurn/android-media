package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {

    @SerializedName("Body")
    @Expose
    private String Body;

    @SerializedName("WebURL")
    @Expose
    private String WebURL;

    @SerializedName("Method")
    @Expose
    private String Method;

    @SerializedName("AppURL")
    @Expose
    private String AppURL;

    public String getBody() {
        return Body;
    }

    public void setBody(String Body) {
        this.Body = Body;
    }

    public String getWebURL() {
        return WebURL;
    }

    public void setWebURL(String WebURL) {
        this.WebURL = WebURL;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String Method) {
        this.Method = Method;
    }

    public String getAppURL() {
        return AppURL;
    }

    public void setAppURL(String AppURL) {
        this.AppURL = AppURL;
    }

    @Override
    public String toString() {
        return "ClassPojo [Body = " + Body + ", WebURL = " + WebURL + ", Method = " + Method + ", AppURL = " + AppURL + "]";
    }
}
