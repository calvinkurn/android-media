package com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hangnadi on 7/5/17.
 */

public class UpdateBy {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("picture")
    private PictureX picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PictureX getPicture() {
        return picture;
    }

    public void setPicture(PictureX picture) {
        this.picture = picture;
    }

}
