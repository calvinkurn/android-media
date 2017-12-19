
package com.tokopedia.events.data.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Form implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("element_type")
    @Expose
    private String elementType;
    @SerializedName("help_text")
    @Expose
    private String helpText;
    @SerializedName("required")
    @Expose
    private Integer required;
    @SerializedName("validator_regex")
    @Expose
    private String validatorRegex;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public Integer getRequired() {
        return required;
    }

    public void setRequired(Integer required) {
        this.required = required;
    }

    public String getValidatorRegex() {
        return validatorRegex;
    }

    public void setValidatorRegex(String validatorRegex) {
        this.validatorRegex = validatorRegex;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.productId);
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeString(this.value);
        dest.writeString(this.elementType);
        dest.writeString(this.helpText);
        dest.writeValue(this.required);
        dest.writeString(this.validatorRegex);
        dest.writeString(this.errorMessage);
        dest.writeValue(this.status);
    }

    public Form() {
    }

    protected Form(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.title = in.readString();
        this.value = in.readString();
        this.elementType = in.readString();
        this.helpText = in.readString();
        this.required = (Integer) in.readValue(Integer.class.getClassLoader());
        this.validatorRegex = in.readString();
        this.errorMessage = in.readString();
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Form> CREATOR = new Parcelable.Creator<Form>() {
        @Override
        public Form createFromParcel(Parcel source) {
            return new Form(source);
        }

        @Override
        public Form[] newArray(int size) {
            return new Form[size];
        }
    };
}
