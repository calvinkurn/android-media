package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 04/12/17.
 */

public class PopUpNotif {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("button_text")
    @Expose
    private String buttonText;
    @SerializedName("button_url")
    @Expose
    private String buttonUrl;
    @SerializedName("app_link")
    @Expose
    private String appLink;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("catalog")
    @Expose
    private Catalog catalog;

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getButtonUrl() {
        return buttonUrl;
    }

    public String getAppLink() {
        return appLink;
    }

    public String getNotes() {
        return notes;
    }

    public Catalog getCatalog() {
        return catalog;
    }
}
