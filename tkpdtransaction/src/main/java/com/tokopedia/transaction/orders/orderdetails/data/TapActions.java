package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TapActions {
    @SerializedName("body")
    @Expose
    private Body Body;

    @SerializedName("value")
    @Expose
    private String Value;

    @SerializedName("weight")
    @Expose
    private int weight;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("control")
    @Expose
    private String Control;

    @SerializedName("name")
    @Expose
    private String Name;

    @SerializedName("buttonType")
    @Expose
    private String buttonType;

    @SerializedName("header")
    @Expose
    private String Header;

    @SerializedName("method")
    @Expose
    private String method;

    @SerializedName("key")
    @Expose
    private String key;

    public Body getBody() {
        return Body;
    }

    public void setBody(Body Body) {
        this.Body = Body;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getControl() {
        return Control;
    }

    public void setControl(String Control) {
        this.Control = Control;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public String getHeader() {
        return Header;
    }

    public void setHeader(String Header) {
        this.Header = Header;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "TapActions [Body = " + Body + ", Value = " + Value + ", weight = " + weight + ", label = " + label + ", Control = " + Control + ", Name = " + Name + ", buttonType = " + buttonType + ", Header = " + Header + ", method = " + method + ", key = " + key + "]";
    }
}
