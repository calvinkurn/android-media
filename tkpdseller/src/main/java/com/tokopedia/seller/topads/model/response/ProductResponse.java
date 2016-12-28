package com.tokopedia.seller.topads.model.response;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.data.Meta;
import com.tokopedia.seller.topads.model.data.Page;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class ProductResponse {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("page")
    @Expose
    private Page page;
    @SerializedName("data")
    @Expose
    private List<Ad> data = new ArrayList<Ad>();

    /**
     *
     * @return
     * The meta
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     *
     * @param meta
     * The meta
     */
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    /**
     *
     * @return
     * The page
     */
    public Page getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(Page page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The data
     */
    public List<Ad> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<Ad> data) {
        this.data = data;
    }

}