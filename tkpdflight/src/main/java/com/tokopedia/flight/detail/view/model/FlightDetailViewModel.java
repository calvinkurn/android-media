package com.tokopedia.flight.detail.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.search.data.cloud.model.response.Route;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.filter.RefundableEnum;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/20/17.
 */

public class FlightDetailViewModel implements ItemType, Parcelable {

    public static final int TYPE = 834;

    private String id;
    private String departureAirport;
    private String departureAirportCity; // merge result
    private String arrivalAirport;
    private String arrivalAirportCity; // merge result
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

    private List<Route> routeList;

    @Override
    public int getType() {
        return TYPE;
    }

    public FlightDetailViewModel build(FlightSearchViewModel flightSearchViewModel){
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
        setRouteList(flightSearchViewModel.getRouteList());
        return this;
    }

    public FlightDetailViewModel build(FlightSearchPassDataViewModel flightSearchPassDataViewModel){
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

    public List<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.departureAirport);
        dest.writeString(this.departureAirportCity);
        dest.writeString(this.arrivalAirport);
        dest.writeString(this.arrivalAirportCity);
        dest.writeInt(this.totalTransit);
        dest.writeString(this.total);
        dest.writeInt(this.totalNumeric);
        dest.writeString(this.beforeTotal);
        dest.writeInt(this.isRefundable == null ? -1 : this.isRefundable.ordinal());
        dest.writeInt(this.adultNumericPrice);
        dest.writeInt(this.childNumericPrice);
        dest.writeInt(this.infantNumericPrice);
        dest.writeInt(this.countAdult);
        dest.writeInt(this.countChild);
        dest.writeInt(this.countInfant);
        dest.writeTypedList(this.routeList);
    }

    public FlightDetailViewModel() {
    }

    protected FlightDetailViewModel(Parcel in) {
        this.id = in.readString();
        this.departureAirport = in.readString();
        this.departureAirportCity = in.readString();
        this.arrivalAirport = in.readString();
        this.arrivalAirportCity = in.readString();
        this.totalTransit = in.readInt();
        this.total = in.readString();
        this.totalNumeric = in.readInt();
        this.beforeTotal = in.readString();
        int tmpIsRefundable = in.readInt();
        this.isRefundable = tmpIsRefundable == -1 ? null : RefundableEnum.values()[tmpIsRefundable];
        this.adultNumericPrice = in.readInt();
        this.childNumericPrice = in.readInt();
        this.infantNumericPrice = in.readInt();
        this.countAdult = in.readInt();
        this.countChild = in.readInt();
        this.countInfant = in.readInt();
        this.routeList = in.createTypedArrayList(Route.CREATOR);
    }

    public static final Parcelable.Creator<FlightDetailViewModel> CREATOR = new Parcelable.Creator<FlightDetailViewModel>() {
        @Override
        public FlightDetailViewModel createFromParcel(Parcel source) {
            return new FlightDetailViewModel(source);
        }

        @Override
        public FlightDetailViewModel[] newArray(int size) {
            return new FlightDetailViewModel[size];
        }
    };
}
