package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {

    @SerializedName("body")
    @Expose
    private String Body;

    @SerializedName("method")
    @Expose
    private String Method;

    @SerializedName("appURL")
    @Expose
    private String AppURL;

    public String getBody() {
        return Body;
    }

    public void setBody(String Body) {
        this.Body = Body;
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
        return "ClassPojo [Body = " + Body + ", Method = " + Method + ", AppURL = " + AppURL + "]";
    }
}
