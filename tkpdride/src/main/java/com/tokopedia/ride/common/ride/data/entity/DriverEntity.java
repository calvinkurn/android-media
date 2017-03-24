package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/24/17.
 */

public class DriverEntity {
//    "driver": {
//        "phone_number": "(555)555-5555",
//                "sms_number": "(555)555-5555",
//                "rating": 5,
//                "picture_url": "https:\/\/d1w2poirtb3as9.cloudfront.net\/img.jpeg",
//                "name": "Bob"
//    },

    @SerializedName("phone_number")
    @Expose
    String phoneNumber;
    @SerializedName("sms_number")
    @Expose
    String smsNumber;
    @SerializedName("rating")
    @Expose
    String rating;
    @SerializedName("picture_url")
    @Expose
    String pictureUrl;
    @SerializedName("name")
    @Expose
    String name;

    public DriverEntity() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSmsNumber() {
        return smsNumber;
    }

    public String getRating() {
        return rating;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getName() {
        return name;
    }
}
