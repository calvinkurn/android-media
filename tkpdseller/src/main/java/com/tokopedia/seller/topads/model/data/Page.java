package com.tokopedia.seller.topads.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class Page {

    @SerializedName("current")
    @Expose
    private Integer current;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("min")
    @Expose
    private Integer min;
    @SerializedName("max")
    @Expose
    private Integer max;
    @SerializedName("total")
    @Expose
    private Integer total;

    /**
     *
     * @return
     * The current
     */
    public Integer getCurrent() {
        return current;
    }

    /**
     *
     * @param current
     * The current
     */
    public void setCurrent(Integer current) {
        this.current = current;
    }

    /**
     *
     * @return
     * The perPage
     */
    public Integer getPerPage() {
        return perPage;
    }

    /**
     *
     * @param perPage
     * The per_page
     */
    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    /**
     *
     * @return
     * The min
     */
    public Integer getMin() {
        return min;
    }

    /**
     *
     * @param min
     * The min
     */
    public void setMin(Integer min) {
        this.min = min;
    }

    /**
     *
     * @return
     * The max
     */
    public Integer getMax() {
        return max;
    }

    /**
     *
     * @param max
     * The max
     */
    public void setMax(Integer max) {
        this.max = max;
    }

    /**
     *
     * @return
     * The total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     *
     * @param total
     * The total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

}
