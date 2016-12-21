package com.tokopedia.seller.topads.model.exchange;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.Meta;
import com.tokopedia.seller.topads.model.data.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/14/16.
 */

public class GroupAdResponse {
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("page")
    @Expose
    private Page page;
    @SerializedName("data")
    @Expose
    private List<GroupAd> data = new ArrayList<>();

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
    public List<GroupAd> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<GroupAd> data) {
        this.data = data;
    }


}
