package com.tokopedia.tkpdtrain.search.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabilla on 3/9/18.
 */

public class TrainScheduleEntity {

    @SerializedName("id")
    @Expose
    private String idSchedule;
    @SerializedName("adult_fare")
    @Expose
    private long adultFare;
    @SerializedName("display_adult_fare")
    @Expose
    private String displayAdultFare;
    @SerializedName("infant_fare")
    @Expose
    private long infantFare;
    @SerializedName("display_infant_fare")
    @Expose
    private String displayInfantFare;
    @SerializedName("arrival_timestamp")
    @Expose
    private String arrivalTimestamp;
    @SerializedName("departure_timestamp")
    @Expose
    private String departureTimestamp;
    @SerializedName("class")
    @Expose
    private String classTrain;
    @SerializedName("display_class")
    @Expose
    private String displayClass;
    @SerializedName("subclass")
    @Expose
    private String subclass;
    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("display_duration")
    @Expose
    private String displayDuration;
    @SerializedName("duration")
    @Expose
    private int duration;
    @SerializedName("train_key")
    @Expose
    private String trainKey;
    @SerializedName("train_name")
    @Expose
    private String trainName;
    @SerializedName("train_no")
    @Expose
    private String trainNumber;

    public String getIdSchedule() {
        return idSchedule;
    }

    public long getAdultFare() {
        return adultFare;
    }

    public String getDisplayAdultFare() {
        return displayAdultFare;
    }

    public long getInfantFare() {
        return infantFare;
    }

    public String getDisplayInfantFare() {
        return displayInfantFare;
    }

    public String getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }

    public String getClassTrain() {
        return classTrain;
    }

    public String getDisplayClass() {
        return displayClass;
    }

    public String getSubclass() {
        return subclass;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDisplayDuration() {
        return displayDuration;
    }

    public int getDuration() {
        return duration;
    }

    public String getTrainKey() {
        return trainKey;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getTrainNumber() {
        return trainNumber;
    }
}


