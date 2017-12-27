package com.tokopedia.seller.product.edit.data.source.cloud.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hendry on 4/18/2017.
 */

public class CategoryRecommRequest {
    @SerializedName("parcel")
    @Expose
    private List<Parcel> parcel = null;
    @SerializedName("size")
    @Expose
    private int size;
    @SerializedName("expect")
    @Expose
    private int expect;
    @SerializedName("score")
    @Expose
    private boolean score;

    public CategoryRecommRequest(List<Parcel> parcel, int expect) {
        this.expect = expect;
        setParcel(parcel);
    }

    public List<Parcel> getParcel() {
        return parcel;
    }

    public void setParcel(List<Parcel> parcel) {
        this.parcel = parcel;
        this.size = this.parcel.size();
    }

    public int getExpect() {
        return expect;
    }

    public void setExpect(int expect) {
        this.expect = expect;
    }

    public boolean getScore() {
        return score;
    }

    public void setScore(boolean score) {
        this.score = score;
    }

}