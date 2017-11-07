
package com.tokopedia.ride.common.ride.domain.model;

import android.os.Bundle;

import org.json.JSONObject;

public class AddPayment {

    private String label;
    private String mode;
    private String saveUrl;
    private String deleteUrl;
    private String method;
    private Boolean active;
    private Bundle saveBody;

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

    public Bundle getSaveBody() {
        return saveBody;
    }

    public void setSaveBody(Bundle saveBody) {
        this.saveBody = saveBody;
    }

}
