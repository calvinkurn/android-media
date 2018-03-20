package com.tokopedia.tkpdtrain.search.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdtrain.search.presentation.adapter.TrainSearchAdapterTypeFactory;

/**
 * Created by nabilla on 3/9/18.
 */

public class TrainScheduleViewModel implements Parcelable, Visitable<TrainSearchAdapterTypeFactory> {

    private String idSchedule;
    private long adultFare;
    private String displayAdultFare;
    private long infantFare;
    private String displayInfantFare;
    private String arrivalTimestamp;
    private String departureTimestamp;
    private String classTrain;
    private String displayClass;
    private String subclass;
    private String origin;
    private String destination;
    private String displayDuration;
    private int duration;
    private String trainKey;
    private String trainName;
    private String trainNumber;
    private int availableSeat;
    private boolean cheapestFlag;
    private boolean fastestFlag;

    public TrainScheduleViewModel() {
    }

    protected TrainScheduleViewModel(Parcel in) {
        idSchedule = in.readString();
        adultFare = in.readLong();
        displayAdultFare = in.readString();
        infantFare = in.readLong();
        displayInfantFare = in.readString();
        arrivalTimestamp = in.readString();
        departureTimestamp = in.readString();
        classTrain = in.readString();
        displayClass = in.readString();
        subclass = in.readString();
        origin = in.readString();
        destination = in.readString();
        displayDuration = in.readString();
        duration = in.readInt();
        trainKey = in.readString();
        trainName = in.readString();
        trainNumber = in.readString();
        availableSeat = in.readInt();
        cheapestFlag = in.readByte() != 0;
        fastestFlag = in.readByte() != 0;
    }

    public static final Creator<TrainScheduleViewModel> CREATOR = new Creator<TrainScheduleViewModel>() {
        @Override
        public TrainScheduleViewModel createFromParcel(Parcel in) {
            return new TrainScheduleViewModel(in);
        }

        @Override
        public TrainScheduleViewModel[] newArray(int size) {
            return new TrainScheduleViewModel[size];
        }
    };

    public String getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(String idSchedule) {
        this.idSchedule = idSchedule;
    }

    public long getAdultFare() {
        return adultFare;
    }

    public void setAdultFare(long adultFare) {
        this.adultFare = adultFare;
    }

    public String getDisplayAdultFare() {
        return displayAdultFare;
    }

    public void setDisplayAdultFare(String displayAdultFare) {
        this.displayAdultFare = displayAdultFare;
    }

    public long getInfantFare() {
        return infantFare;
    }

    public void setInfantFare(long infantFare) {
        this.infantFare = infantFare;
    }

    public String getDisplayInfantFare() {
        return displayInfantFare;
    }

    public void setDisplayInfantFare(String displayInfantFare) {
        this.displayInfantFare = displayInfantFare;
    }

    public String getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public void setArrivalTimestamp(String arrivalTimestamp) {
        this.arrivalTimestamp = arrivalTimestamp;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }

    public void setDepartureTimestamp(String departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
    }

    public String getClassTrain() {
        return classTrain;
    }

    public void setClassTrain(String classTrain) {
        this.classTrain = classTrain;
    }

    public String getDisplayClass() {
        return displayClass;
    }

    public void setDisplayClass(String displayClass) {
        this.displayClass = displayClass;
    }

    public String getSubclass() {
        return subclass;
    }

    public void setSubclass(String subclass) {
        this.subclass = subclass;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDisplayDuration() {
        return displayDuration;
    }

    public void setDisplayDuration(String displayDuration) {
        this.displayDuration = displayDuration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTrainKey() {
        return trainKey;
    }

    public void setTrainKey(String trainKey) {
        this.trainKey = trainKey;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public int getAvailableSeat() {
        return availableSeat;
    }

    public void setAvailableSeat(int availableSeat) {
        this.availableSeat = availableSeat;
    }



    public boolean isCheapestFlag() {
        return cheapestFlag;
    }

    public void setCheapestFlag(boolean cheapestFlag) {
        this.cheapestFlag = cheapestFlag;
    }

    public boolean isFastestFlag() {
        return fastestFlag;
    }

    public void setFastestFlag(boolean fastestFlag) {
        this.fastestFlag = fastestFlag;
    }

    @Override
    public int type(TrainSearchAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idSchedule);
        parcel.writeLong(adultFare);
        parcel.writeString(displayAdultFare);
        parcel.writeLong(infantFare);
        parcel.writeString(displayInfantFare);
        parcel.writeString(arrivalTimestamp);
        parcel.writeString(departureTimestamp);
        parcel.writeString(classTrain);
        parcel.writeString(displayClass);
        parcel.writeString(subclass);
        parcel.writeString(origin);
        parcel.writeString(destination);
        parcel.writeString(displayDuration);
        parcel.writeInt(duration);
        parcel.writeString(trainKey);
        parcel.writeString(trainName);
        parcel.writeString(trainNumber);
        parcel.writeInt(availableSeat);
        parcel.writeByte((byte) (cheapestFlag ? 1 : 0));
        parcel.writeByte((byte) (fastestFlag ? 1 : 0));
    }

    @Override
    public String toString() {
        return "Schedule ID: " + idSchedule + "\n" +
                "Adult Fare: " + displayAdultFare  + "\n" +
                "Train Name: " + trainName + "\n" +
                "Duration: " + duration + "\n" +
                "Departure Timestamp: " + departureTimestamp + "\n" +
                "Arrival Timestamp: " + arrivalTimestamp + "\n" +
                "Availibility: " + availableSeat + "\n";
    }
}