package com.tokopedia.topads.dashboard.data.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class Meta {

    @SerializedName("page")
    @Expose
    private Page page;

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

}
