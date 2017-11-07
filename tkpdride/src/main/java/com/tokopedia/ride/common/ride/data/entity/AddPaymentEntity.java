
package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class AddPaymentEntity {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("save_url")
    @Expose
    private String saveUrl;
    @SerializedName("delete_url")
    @Expose
    private String deleteUrl;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("save_body")
    @Expose
    private JsonObject saveBody;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    public String getDeleteUrl() {
        return deleteUrl;
    }

    public void setDeleteUrl(String deleteUrl) {
        this.deleteUrl = deleteUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public JsonObject getSaveBody() {
        return saveBody;
    }

    public void setSaveBody(JsonObject saveBody) {
        this.saveBody = saveBody;
    }

}
