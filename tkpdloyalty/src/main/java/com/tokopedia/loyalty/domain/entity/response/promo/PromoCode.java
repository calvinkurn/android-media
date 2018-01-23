package com.tokopedia.loyalty.domain.entity.response.promo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 09/01/18.
 */

public class PromoCode {

    @SerializedName("group_code_title")
    @Expose
    private String groupCodeTitle;
    @SerializedName("group_code_description")
    @Expose
    private String groupCodeDescription;
    @SerializedName("group_code")
    @Expose
    private List<GroupCode> groupCode = new ArrayList<>();

    public String getGroupCodeTitle() {
        return groupCodeTitle;
    }

    public String getGroupCodeDescription() {
        return groupCodeDescription;
    }

    public List<GroupCode> getGroupCode() {
        return groupCode;
    }
}
