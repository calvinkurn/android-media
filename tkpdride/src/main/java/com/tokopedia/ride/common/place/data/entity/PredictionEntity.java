package com.tokopedia.ride.common.place.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 3/18/17.
 */

public class PredictionEntity {
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("place_id")
    @Expose
    String placeId;
    @SerializedName("reference")
    @Expose
    String reference;
    @SerializedName("types")
    @Expose
    List<String> types;
    @SerializedName("matched_substrings")
    @Expose
    List<String> matchedSubsctrings;

}
