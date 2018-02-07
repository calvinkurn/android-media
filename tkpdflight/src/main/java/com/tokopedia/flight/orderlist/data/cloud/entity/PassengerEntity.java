package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 12/6/17.
 */

public class PassengerEntity {
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("title")
    @Expose
    private int title;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("passport_no")
    @Expose
    private String passportNo;
    @SerializedName("passport_country")
    @Expose
    private String passportCountry;
    @SerializedName("passport_expiry")
    @Expose
    private String passportExpiry;
    @SerializedName("amenities")
    @Expose
    private List<PassengerAmentityEntity> amenities;

    public int getType() {
        return type;
    }

    public int getTitle() {
        return title;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDob() {
        return dob;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public String getPassportCountry() {
        return passportCountry;
    }

    public String getPassportExpiry() {
        return passportExpiry;
    }

    public List<PassengerAmentityEntity> getAmenities() {
        return amenities;
    }
}
