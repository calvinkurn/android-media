
package com.tokopedia.seller.shopscore.data.source.cloud.model.summary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailData {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Value")
    @Expose
    private Integer value;
    @SerializedName("Description")
    @Expose
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
