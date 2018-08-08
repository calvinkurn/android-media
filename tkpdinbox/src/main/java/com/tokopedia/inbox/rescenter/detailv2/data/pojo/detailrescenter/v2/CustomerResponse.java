package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 07/11/17.
 */
public class CustomerResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("picture")
    private PictureResponse picture;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PictureResponse getPicture() {
        return picture;
    }

    public void setPicture(PictureResponse picture) {
        this.picture = picture;
    }
}
