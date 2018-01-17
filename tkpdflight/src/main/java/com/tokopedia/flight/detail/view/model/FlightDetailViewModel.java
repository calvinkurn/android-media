package com.tokopedia.flight.detail.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.filter.RefundableEnum;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/20/17.
 */

public class FlightDetailViewModel implements Parcelable {

    public static final Creator<FlightDetailViewModel> CREATOR = new Creator<FlightDetailViewModel>() {
        @Override
        public FlightDetailViewModel createFromParcel(Parcel in) {
            return new FlightDetailViewModel(in);
        }

        @Override
        public FlightDetailViewModel[] newArray(int size) {
            return new FlightDetailViewModel[size];
        }
    };
    private String id;
    private String departureAirport;
    private String departureAirportCity; // merge result
    private String departureTime;
    private String arrivalAirport;
    private String arrivalAirportCity; // merge result
    private String arrivalTime; // merge result
    private int totalTransit;
    private String total; // 693000
    private int totalNumeric; // Fare "Rp 693.000"
    private String beforeTotal;
    private RefundableEnum isRefundable;
    private int adultNumericPrice;
    private int childNumericPrice;
    private int infantNumericPrice;
    private int countAdult;
    private int countChild;
    private int countInfant;
    private List<FlightDetailRouteViewModel> routeList;

    protected FlightDetailViewModel(Parcel in) {
        id = in.readString();
        departureAirport = in.readString();
        departureAirportCity = in.readString();
        departureTime = in.readString();
        arrivalAirport = in.readString();
        arrivalAirportCity = in.readString();
        arrivalTime = in.readString();
        totalTransit = in.readInt();
        total = in.readString();
        totalNumeric = in.readInt();
        beforeTotal = in.readString();
        adultNumericPrice = in.readInt();
        childNumericPrice = in.readInt();
        infantNumericPrice = in.readInt();
        countAdult = in.readInt();
        countChild = in.readInt();
        countInfant = in.readInt();
        routeList = in.createTypedArrayList(FlightDetailRouteViewModel.CREATOR);
    }

    public FlightDetailViewModel() {
    }

    public FlightDetailViewModel build(FlightSearchViewModel flightSearchViewModel) {
        if (flightSearchViewModel != null) {
            setId(flightSearchViewModel.getId());
            setDepartureAirport(flightSearchViewModel.getDepartureAirport());
            setDepartureAirportCity(flightSearchViewModel.getDepartureAirportCity());
            setArrivalAirport(flightSearchViewModel.getArrivalAirport());
            setArrivalAirportCity(flightSearchViewModel.getArrivalAirportCity());
            setTotalTransit(flightSearchViewModel.getTotalTransit());
            setTotal(flightSearchViewModel.getTotal());
            setTotalNumeric(flightSearchViewModel.getTotalNumeric());
            setBeforeTotal(flightSearchViewModel.getBeforeTotal());
            setIsRefundable(flightSearchViewModel.isRefundable());
            setAdultNumericPrice(flightSearchViewModel.getFare().getAdultNumeric());
            setChildNumericPrice(flightSearchViewModel.getFare().getChildNumeric());
            setInfantNumericPrice(flightSearchViewModel.getFare().getInfantNumeric());
            FlightDetailRouteInfoViewModelMapper flightDetailRouteInfoViewModelMapper = new FlightDetailRouteInfoViewModelMapper();
            FlightDetailRouteViewModelMapper mapper = new FlightDetailRouteViewModelMapper(flightDetailRouteInfoViewModelMapper);
            setRouteList(mapper.transform(flightSearchViewModel.getRouteList(), flightSearchViewModel.getAirlineList()));
            setDepartureTime(flightSearchViewModel.getDepartureTime());
            setArrivalTime(flightSearchViewModel.getArrivalTime());
            return this;
        } else {
            return null;
        }
    }

    public FlightDetailViewModel build(FlightSearchPassDataViewModel flightSearchPassDataViewModel) {
        setCountAdult(flightSearchPassDataViewModel.getFlightPassengerViewModel().getAdult());
        setCountChild(flightSearchPassDataViewModel.getFlightPassengerViewModel().getChildren());
        setCountInfant(flightSearchPassDataViewModel.getFlightPassengerViewModel().getInfant());
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getDepartureAirportCity() {
        return departureAirportCity;
    }

    public void setDepartureAirportCity(String departureAirportCity) {
        this.departureAirportCity = departureAirportCity;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getArrivalAirportCity() {
        return arrivalAirportCity;
    }

    public void setArrivalAirportCity(String arrivalAirportCity) {
        this.arrivalAirportCity = arrivalAirportCity;
    }

    public int getTotalTransit() {
        return totalTransit;
    }

    public void setTotalTransit(int totalTransit) {
        this.totalTransit = totalTransit;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getTotalNumeric() {
        return totalNumeric;
    }

    public void setTotalNumeric(int totalNumeric) {
        this.totalNumeric = totalNumeric;
    }

    public String getBeforeTotal() {
        return beforeTotal;
    }

    public void setBeforeTotal(String beforeTotal) {
        this.beforeTotal = beforeTotal;
    }

    public RefundableEnum getIsRefundable() {
        return isRefundable;
    }

    public void setIsRefundable(RefundableEnum isRefundable) {
        this.isRefundable = isRefundable;
    }

    public int getAdultNumericPrice() {
        return adultNumericPrice;
    }

    public void setAdultNumericPrice(int adultNumericPrice) {
        this.adultNumericPrice = adultNumericPrice;
    }

    public int getChildNumericPrice() {
        return childNumericPrice;
    }

    public void setChildNumericPrice(int childNumericPrice) {
        this.childNumericPrice = childNumericPrice;
    }

    public int getInfantNumericPrice() {
        return infantNumericPrice;
    }

    public void setInfantNumericPrice(int infantNumericPrice) {
        this.infantNumericPrice = infantNumericPrice;
    }

    public int getCountAdult() {
        return countAdult;
    }

    public void setCountAdult(int countAdult) {
        this.countAdult = countAdult;
    }

    public int getCountChild() {
        return countChild;
    }

    public void setCountChild(int countChild) {
        this.countChild = countChild;
    }

    public int getCountInfant() {
        return countInfant;
    }

    public void setCountInfant(int countInfant) {
        this.countInfant = countInfant;
    }

    public List<FlightDetailRouteViewModel> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<FlightDetailRouteViewModel> routeList) {
        this.routeList = routeList;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(departureAirport);
        parcel.writeString(departureAirportCity);
        parcel.writeString(departureTime);
        parcel.writeString(arrivalAirport);
        parcel.writeString(arrivalAirportCity);
        parcel.writeString(arrivalTime);
        parcel.writeInt(totalTransit);
        parcel.writeString(total);
        parcel.writeInt(totalNumeric);
        parcel.writeString(beforeTotal);
        parcel.writeInt(adultNumericPrice);
        parcel.writeInt(childNumericPrice);
        parcel.writeInt(infantNumericPrice);
        parcel.writeInt(countAdult);
        parcel.writeInt(countChild);
        parcel.writeInt(countInfant);
        parcel.writeTypedList(routeList);
    }
}
