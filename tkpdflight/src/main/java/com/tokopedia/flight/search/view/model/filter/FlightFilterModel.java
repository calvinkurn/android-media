package com.tokopedia.flight.search.view.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11/1/2017.
 */

public class FlightFilterModel implements Parcelable, Cloneable {
    public static final Creator<FlightFilterModel> CREATOR = new Creator<FlightFilterModel>() {
        @Override
        public FlightFilterModel createFromParcel(Parcel in) {
            return new FlightFilterModel(in);
        }

        @Override
        public FlightFilterModel[] newArray(int size) {
            return new FlightFilterModel[size];
        }
    };
    private int priceMin = Integer.MIN_VALUE;
    private int priceMax = Integer.MAX_VALUE;
    private int durationMin = Integer.MIN_VALUE;
    private int durationMax = Integer.MAX_VALUE;
    private List<TransitEnum> transitTypeList;
    private List<String> airlineList;
    private List<DepartureTimeEnum> departureTimeList;
    private List<RefundableEnum> refundableTypeList;
    private boolean isHasFilter = false;

    protected FlightFilterModel(Parcel in) {
        priceMin = in.readInt();
        priceMax = in.readInt();
        durationMin = in.readInt();
        durationMax = in.readInt();
        airlineList = in.createStringArrayList();
        isHasFilter = in.readByte() != 0;
    }

    public FlightFilterModel() {
    }

    public int getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public int getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(int priceMax) {
        this.priceMax = priceMax;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public int getDurationMax() {
        return durationMax;
    }

    public void setDurationMax(int durationMax) {
        this.durationMax = durationMax;
    }

    public List<String> getAirlineList() {
        return airlineList;
    }

    public void setAirlineList(List<String> airlineList) {
        this.airlineList = airlineList;
    }

    public List<TransitEnum> getTransitTypeList() {
        return transitTypeList;
    }

    public void setTransitTypeList(List<TransitEnum> transitTypeList) {
        this.transitTypeList = transitTypeList;
    }

    public List<DepartureTimeEnum> getDepartureTimeList() {
        return departureTimeList;
    }

    public void setDepartureTimeList(List<DepartureTimeEnum> departureTimeList) {
        this.departureTimeList = departureTimeList;
    }

    public List<RefundableEnum> getRefundableTypeList() {
        return refundableTypeList;
    }

    public void setRefundableTypeList(List<RefundableEnum> refundableTypeList) {
        this.refundableTypeList = refundableTypeList;
    }

    public boolean hasFilter() {
        return isHasFilter;
    }

    public FlightFilterModel copy() {
        FlightFilterModel flightFilterModel = new FlightFilterModel();
        flightFilterModel.setPriceMin(getPriceMin());
        flightFilterModel.setPriceMax(getPriceMax());
        flightFilterModel.setDurationMin(getDurationMin());
        flightFilterModel.setDurationMax(getDurationMax());
        flightFilterModel.setTransitTypeList(getCopyOfTransitList());
        flightFilterModel.setAirlineList(getCopyOfAirlineList());
        flightFilterModel.setDepartureTimeList(getCopyOfDepartureList());
        flightFilterModel.setRefundableTypeList(getCopyOfRefundableList());
        return flightFilterModel;
    }

    private List<TransitEnum> getCopyOfTransitList(){
        List<TransitEnum> transitEnumList = new ArrayList<>();
        if (getTransitTypeList()!=null) {
            for (int i = 0, sizei = getTransitTypeList().size(); i < sizei; i++) {
                transitEnumList.add(getTransitTypeList().get(i));
            }
        }
        return transitEnumList;
    }

    private List<DepartureTimeEnum> getCopyOfDepartureList(){
        List<DepartureTimeEnum> departureTimeEnumList = new ArrayList<>();
        if (getDepartureTimeList()!= null) {
            for (int i = 0, sizei = getDepartureTimeList().size(); i < sizei; i++) {
                departureTimeEnumList.add(getDepartureTimeList().get(i));
            }
        }
        return departureTimeEnumList;
    }

    private List<String> getCopyOfAirlineList(){
        List<String> airlineList = new ArrayList<>();
        if (getAirlineList()!= null) {
            for (int i = 0, sizei = getAirlineList().size(); i < sizei; i++) {
                airlineList.add(getAirlineList().get(i));
            }
        }
        return airlineList;
    }

    private List<RefundableEnum> getCopyOfRefundableList(){
        List<RefundableEnum> refundableEnumList = new ArrayList<>();
        if (getRefundableTypeList()!=null) {
            for (int i = 0, sizei = getRefundableTypeList().size(); i < sizei; i++) {
                refundableEnumList.add(getRefundableTypeList().get(i));
            }
        }
        return refundableEnumList;
    }

    public void setHasFilter(FlightSearchStatisticModel flightSearchStatisticModel) {
        int priceMinStat;
        int priceMaxStat;
        int durMinStat;
        int durMaxStat;

        if (flightSearchStatisticModel == null) {
            priceMinStat = Integer.MIN_VALUE;
            priceMaxStat = Integer.MAX_VALUE;
            durMinStat = Integer.MIN_VALUE;
            durMaxStat = Integer.MAX_VALUE;
        } else {
            priceMinStat = flightSearchStatisticModel.getMinPrice();
            priceMaxStat = flightSearchStatisticModel.getMaxPrice();
            durMinStat = flightSearchStatisticModel.getMinDuration();
            durMaxStat = flightSearchStatisticModel.getMaxDuration();
        }
        isHasFilter = (this.priceMin > priceMinStat || this.priceMax < priceMaxStat ||
                this.durationMin > durMinStat || this.durationMax < durMaxStat ||
                (this.transitTypeList != null && this.transitTypeList.size() > 0) ||
                (this.airlineList != null && this.airlineList.size() > 0) ||
                (this.departureTimeList != null && this.departureTimeList.size() > 0) ||
                (this.refundableTypeList != null && this.refundableTypeList.size() > 0));
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(priceMin);
        parcel.writeInt(priceMax);
        parcel.writeInt(durationMin);
        parcel.writeInt(durationMax);
        parcel.writeStringList(airlineList);
        parcel.writeByte((byte) (isHasFilter ? 1 : 0));
    }
}
