package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 12/8/17. Tokopedia
 */

public class DigitalVoucherData {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private DigitalAttributesVoucher attributes;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public DigitalAttributesVoucher getAttributes() {
        return attributes;
    }


}
