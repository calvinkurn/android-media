package com.tokopedia.tkpdtrain.search.data.databasetable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.tkpdtrain.common.database.TkpdTrainDatabase;

/**
 * Created by nabillasabbaha on 3/12/18.
 */
@Table(database = TkpdTrainDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class TrainScheduleDbTable extends BaseModel {
    @PrimaryKey
    @Column(name = "schedule_id")
    private
    String idSchedule;
    @Column(name = "adult_fare")
    private
    long adultFare;
    @Column(name = "display_adult_fare")
    private
    String displayAdultFare;
    @Column(name = "infant_fare")
    private
    long infantFare;
    @Column(name = "display_infant_fare")
    private
    String displayInfantFare;
    @Column(name = "arrival_timestamp")
    private
    String arrivalTimestamp;
    @Column(name = "departure_timestamp")
    private
    String departureTimestamp;
    @Column(name = "train_class")
    private
    String classTrain;
    @Column(name = "display_class")
    private
    String displayClass;
    @Column(name = "subclass")
    private
    String subclass;
    @Column(name = "origin")
    private
    String origin;
    @Column(name = "destination")
    private
    String destination;
    @Column(name = "display_duration")
    private
    String displayDuration;
    @Column(name = "duration")
    private
    int duration;
    @Column(name = "train_key")
    private
    String trainKey;
    @Column(name = "train_name")
    private
    String trainName;
    @Column(name = "train_no")
    private
    String trainNumber;
    @Column(name = "available_seat")
    private
    int availableSeat;
    @Column(name = "cheapest_flag")
    private
    boolean cheapestFlag;
    @Column(name = "fastest_flag")
    private
    boolean fastestFlag;

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
}
