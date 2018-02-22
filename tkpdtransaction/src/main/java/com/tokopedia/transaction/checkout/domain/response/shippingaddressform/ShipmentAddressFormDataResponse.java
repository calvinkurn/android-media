package com.tokopedia.transaction.checkout.domain.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class ShipmentAddressFormDataResponse {
    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<>();
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("is_multiple")
    @Expose
    private int isMultiple;
    @SerializedName("group_address")
    @Expose
    private List<GroupAddress> groupAddress = new ArrayList<>();
    @SerializedName("kero_token")
    @Expose
    private String keroToken;
    @SerializedName("kero_discom_token")
    @Expose
    private String keroDiscomToken;
    @SerializedName("kero_unix_time")
    @Expose
    private int keroUnixTime;

    public List<String> getErrors() {
        return errors;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getIsMultiple() {
        return isMultiple;
    }

    public List<GroupAddress> getGroupAddress() {
        return groupAddress;
    }

    public String getKeroToken() {
        return keroToken;
    }

    public String getKeroDiscomToken() {
        return keroDiscomToken;
    }

    public int getKeroUnixTime() {
        return keroUnixTime;
    }
}
