package com.tokopedia.transaction.checkout.view.data.cartshipmentform;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class CartShipmentAddressFormData {

    private List<String> errors = new ArrayList<>();
    private int errorCode;
    private boolean isMultiple;
    private List<GroupAddress> groupAddress = new ArrayList<>();
    private String keroToken;
    private String keroDiscomToken;
    private int keroUnixTime;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    public List<GroupAddress> getGroupAddress() {
        return groupAddress;
    }

    public void setGroupAddress(List<GroupAddress> groupAddress) {
        this.groupAddress = groupAddress;
    }

    public String getKeroToken() {
        return keroToken;
    }

    public void setKeroToken(String keroToken) {
        this.keroToken = keroToken;
    }

    public String getKeroDiscomToken() {
        return keroDiscomToken;
    }

    public void setKeroDiscomToken(String keroDiscomToken) {
        this.keroDiscomToken = keroDiscomToken;
    }

    public int getKeroUnixTime() {
        return keroUnixTime;
    }

    public void setKeroUnixTime(int keroUnixTime) {
        this.keroUnixTime = keroUnixTime;
    }
}
