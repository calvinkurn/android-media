package com.tokopedia.seller.product.variant.data.model.variantsubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * metrics between  the variant of level 1 + level 2 + so on.
 * Created by hendry on 8/15/2017.
 */

public class ProductVariant {

    @SerializedName("st")
    @Expose
    private int st;
    @SerializedName("opt")
    @Expose
    private List<Integer> optList = null;
    @SerializedName("pvd")
    @Expose
    private int pvd;

    /**
     * get status of this metrics
     * @return 0 if not available. 1 if available.
     */
    public int getSt() {
        return st;
    }

    /**
     * status of the variant. 0: not available. 1: available
     * @param st set 0 for not available (or zero stock)
     */
    public void setSt(int st) {
        this.st = st;
    }

    /**
     * get the list of combination between t_id of variant level 1 + variant level 2 + so on
     * @return optList
     */
    public List<Integer> getOptList() {
        return optList;
    }

    /**
     * set this list with combination between t_id of variant level 1 + variant level 2 + so on
     * @param optList
     */
    public void setOptList(List<Integer> optList) {
        this.optList = optList;
    }

    /**
     * product variant data id (got from the server)
     * @return "0" if this metric is just new created. "1" if this metric is existing from server
     */
    public int getPvd() {
        return pvd;
    }

    /**
     * set with the pvd got from the server
     * set with 0 if this is new created.
     * @param pvd "0" if new created. pvd id if this is got from server.
     */
    public void setPvd(int pvd) {
        this.pvd = pvd;
    }

}
