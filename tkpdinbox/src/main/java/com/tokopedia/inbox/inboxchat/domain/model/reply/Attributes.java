
package com.tokopedia.inbox.inboxchat.domain.model.reply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("domain")
    @Expose
    private String domain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

}
