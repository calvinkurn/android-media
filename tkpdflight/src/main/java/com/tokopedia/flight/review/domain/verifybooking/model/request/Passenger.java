
package com.tokopedia.flight.review.domain.verifybooking.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Passenger {

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
    /*@SerializedName("nationality")
    @Expose
    private String nationality;*/
    /*@SerializedName("passport_no")
    @Expose
    private String passportNo;
    @SerializedName("passport_country")
    @Expose
    private String passportCountry;
    @SerializedName("passport_expiry")
    @Expose
    private String passportExpiry;*/
    @SerializedName("amenities")
    @Expose
    private List<AmenityPassenger> amenities = new ArrayList<>();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    /*public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }*/

    /*public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPassportCountry() {
        return passportCountry;
    }

    public void setPassportCountry(String passportCountry) {
        this.passportCountry = passportCountry;
    }

    public String getPassportExpiry() {
        return passportExpiry;
    }

    public void setPassportExpiry(String passportExpiry) {
        this.passportExpiry = passportExpiry;
    }*/

    public List<AmenityPassenger> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<AmenityPassenger> amenities) {
        this.amenities = amenities;
    }

}
