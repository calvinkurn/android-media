package com.tokopedia.seller.product.variant.data.model.variantbyprd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 8/15/2017.
 */

public class VariantOption {

    @SerializedName("pv_id")
    @Expose
    private int pvId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("unit_name")
    @Expose
    private String unitName;
    @SerializedName("v_id")
    @Expose
    private int vId;
    @SerializedName("vu_id")
    @Expose
    private int vuId;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("position")
    @Expose
    private int position;
    @SerializedName("option")
    @Expose
    private List<Option> option = null;

    public Integer getPvId() {
        return pvId;
    }

    public void setPvId(int pvId) {
        this.pvId = pvId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getVId() {
        return vId;
    }

    public void setVId(int vId) {
        this.vId = vId;
    }

    public int getVuId() {
        return vuId;
    }

    public void setVuId(int vuId) {
        this.vuId = vuId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Option> getOption() {
        return option;
    }

    public void setOption(List<Option> option) {
        this.option = option;
    }

}
