package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionButton {

    @SerializedName("Body")
    @Expose
    private Body Body;

    @SerializedName("Value")
    @Expose
    private String Value;

    @SerializedName("weight")
    @Expose
    private String weight;

    @SerializedName("uriWeb")
    @Expose
    private String uriWeb;

    @SerializedName("mappingUri")
    @Expose
    private String mappingUri;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("uri")
    @Expose
    private String uri;

    @SerializedName("Control")
    @Expose
    private String Control;

    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("buttonType")
    @Expose
    private String buttonType;

    @SerializedName("Header")
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUriWeb() {
        return uriWeb;
    }

    public void setUriWeb(String uriWeb) {
        this.uriWeb = uriWeb;
    }

    public String getMappingUri() {
        return mappingUri;
    }

    public void setMappingUri(String mappingUri) {
        this.mappingUri = mappingUri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
        return "ActionButton [Body = " + Body + ", Value = " + Value + ", weight = " + weight + ", uriWeb = " + uriWeb + ", mappingUri = " + mappingUri + ", label = " + label + ", uri = " + uri + ", Control = " + Control + ", Name = " + Name + ", buttonType = " + buttonType + ", Header = " + Header + ", method = " + method + ", key = " + key + "]";
    }
}