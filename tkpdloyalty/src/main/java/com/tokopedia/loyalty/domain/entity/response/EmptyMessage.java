package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author  by alvarisi on 4/4/18.
 */

public class EmptyMessage {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub_title")
    @Expose
    private String subTitle;

    public EmptyMessage() {
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }
}
