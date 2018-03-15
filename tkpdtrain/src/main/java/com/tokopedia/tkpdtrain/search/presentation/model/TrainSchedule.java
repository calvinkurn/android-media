package com.tokopedia.tkpdtrain.search.presentation.model;

/**
 * Created by nabilla on 3/9/18.
 */

public class TrainSchedule {

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

    public TrainSchedule() {
    }

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