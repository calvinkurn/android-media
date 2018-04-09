package com.tokopedia.flight.cancellation.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by furqan on 23/03/18.
 */

public class FlightCancellationJourney implements Parcelable {

    private String journeyId;
    private String departureCity;
    private String departureCityCode;
    private String departureAiportId;
    private String departureTime;
    private String arrivalCity;
    private String arrivalCityCode;
    private String arrivalAirportId;
    private String arrivalTime;
    private String airlineName;
    private boolean isRefundable;

    public FlightCancellationJourney() {
    }

    protected FlightCancellationJourney(Parcel in) {
        journeyId = in.readString();
        departureCity = in.readString();
        departureCityCode = in.readString();
        departureAiportId = in.readString();
        departureTime = in.readString();
        arrivalCity = in.readString();
        arrivalCityCode = in.readString();
        arrivalAirportId = in.readString();
        arrivalTime = in.readString();
        airlineName = in.readString();
        isRefundable = in.readByte() != 0;
    }

    public static final Creator<FlightCancellationJourney> CREATOR = new Creator<FlightCancellationJourney>() {
        @Override
        public FlightCancellationJourney createFromParcel(Parcel in) {
            return new FlightCancellationJourney(in);
        }

        @Override
        public FlightCancellationJourney[] newArray(int size) {
            return new FlightCancellationJourney[size];
        }
    };

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getDepartureCityCode() {
        return departureCityCode;
    }

    public void setDepartureCityCode(String departureCityCode) {
        this.departureCityCode = departureCityCode;
    }

    public String getDepartureAiportId() {
        return departureAiportId;
    }

    public void setDepartureAiportId(String departureAiportId) {
        this.departureAiportId = departureAiportId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getArrivalCityCode() {
        return arrivalCityCode;
    }

    public void setArrivalCityCode(String arrivalCityCode) {
        this.arrivalCityCode = arrivalCityCode;
    }

    public String getArrivalAirportId() {
        return arrivalAirportId;
    }

    public void setArrivalAirportId(String arrivalAirportId) {
        this.arrivalAirportId = arrivalAirportId;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public boolean isRefundable() {
        return isRefundable;
    }

    public void setRefundable(boolean refundable) {
        isRefundable = refundable;
    }

    @Override
    public String toString() {
        return "FlightCancellationJourney{" +
                "journeyId='" + journeyId + '\'' +
                ", departureCity='" + departureCity + '\'' +
                ", departureCityCode='" + departureCityCode + '\'' +
                ", departureAiportId='" + departureAiportId + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalCity='" + arrivalCity + '\'' +
                ", arrivalCityCode='" + arrivalCityCode + '\'' +
                ", arrivalAirportId='" + arrivalAirportId + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", airlineName='" + airlineName + '\'' +
                ", isRefundable=" + isRefundable +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(journeyId);
        dest.writeString(departureCity);
        dest.writeString(departureCityCode);
        dest.writeString(departureAiportId);
        dest.writeString(departureTime);
        dest.writeString(arrivalCity);
        dest.writeString(arrivalCityCode);
        dest.writeString(arrivalAirportId);
        dest.writeString(arrivalTime);
        dest.writeString(airlineName);
        dest.writeByte((byte) (isRefundable ? 1 : 0));
    }
}
