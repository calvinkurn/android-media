package com.tokopedia.session.session.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 17/11/2015.
 */
@Parcel
public class UserRepModel {
    @SerializedName("positive_percentage")
    String positivePercentage;//positive_percentage
    @SerializedName("no_reputation")
    String noReputation;//no_reputation
    @SerializedName("positive")
    String positive;//positive
    @SerializedName("negative")
    String negative;//negative
    @SerializedName("neutral")
    String neutral;//neutral
    // end of user reputation
}
