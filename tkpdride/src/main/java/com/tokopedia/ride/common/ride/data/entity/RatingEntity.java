package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 6/5/17.
 */

public class RatingEntity {
    @SerializedName("stars")
    @Expose
    private String stars;
    @SerializedName("comment")
    @Expose
    private String comment;

    public String getStars() {
        return stars;
    }

    public String getComment() {
        return comment;
    }
}
