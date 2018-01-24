package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by alvarisi on 12/6/17.
 */

public class FlightEntity {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("total_adult")
    @Expose
    private String totalAdult;
    @SerializedName("total_adult_numeric")
    @Expose
    private int totalAdultNumeric;
    @SerializedName("total_child")
    @Expose
    private String totalChild;
    @SerializedName("total_child_numeric")
    @Expose
    private int totalChildNumeric;
    @SerializedName("total_infant")
    @Expose
    private String totalInfant;
    @SerializedName("total_infant_numeric")
    @Expose
    private int totalInfantNumeric;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("pdf")
    @Expose
    private String pdf;
    @SerializedName("journeys")
    @Expose
    private List<JourneyEntity> journeys;
    @SerializedName("passengers")
    @Expose
    private List<PassengerEntity> passengers;
    @SerializedName("payment")
    @Expose
    private PaymentInfoEntity payment;

    public FlightEntity() {
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getTotalAdult() {
        return totalAdult;
    }

    public String getTotalChild() {
        return totalChild;
    }

    public String getTotalInfant() {
        return totalInfant;
    }

    public String getCurrency() {
        return currency;
    }

    public List<JourneyEntity> getJourneys() {
        return journeys;
    }

    public List<PassengerEntity> getPassengers() {
        return passengers;
    }

    public int getTotalAdultNumeric() {
        return totalAdultNumeric;
    }

    public int getTotalChildNumeric() {
        return totalChildNumeric;
    }

    public int getTotalInfantNumeric() {
        return totalInfantNumeric;
    }

    public PaymentInfoEntity getPayment() {
        return payment;
    }

    public String getPdf() {
        return pdf;
    }
}
