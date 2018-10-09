package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sachinbansal on 5/16/18.
 */

public class GqlTokoPointPopupNotif {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("imageURL")
    @Expose
    private String imageURL;

    @SerializedName("buttonText")
    @Expose
    private String buttonText;

    @SerializedName("buttonURL")
    @Expose
    private String buttonURL;

    @SerializedName("appLink")
    @Expose
    private String appLink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonURL() {
        return buttonURL;
    }

    public void setButtonURL(String buttonURL) {
        this.buttonURL = buttonURL;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }
}
