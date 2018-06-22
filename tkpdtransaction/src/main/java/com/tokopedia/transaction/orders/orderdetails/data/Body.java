package com.tokopedia.transaction.orders.orderdetails.data;

public class Body {
    private String Body;

    private String WebURL;

    private String Method;

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
