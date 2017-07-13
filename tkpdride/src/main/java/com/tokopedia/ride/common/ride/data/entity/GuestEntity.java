package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/27/17.
 */

public class GuestEntity {
    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("first_name")
    @Expose
    String firstName;
    @SerializedName("last_name")
    @Expose
    String lastName;
    @SerializedName("guest_id")
    @Expose
    String guestId;
    @SerializedName("phone_number")
    @Expose
    String phoneNumber;

    public GuestEntity() {
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGuestId() {
        return guestId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
