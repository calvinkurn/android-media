package com.tokopedia.flight.orderlist.domain.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.detail.view.adapter.FlightDetailOrderTypeFactory;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;

import java.util.List;

/**
 * @author by alvarisi on 12/11/17.
 */

public class FlightOrderJourney implements Visitable<FlightDetailOrderTypeFactory> {

    private String departureCity;
    private String departureCityCode;
    private String departureAiportId;
    private String departureTime;
    private String arrivalCity;
    private String arrivalCityCode;
    private String arrivalAirportId;
    private String arrivalTime;
    private String status;
    private List<FlightDetailRouteViewModel> routeViewModels;

    public FlightOrderJourney() {
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FlightDetailRouteViewModel> getRouteViewModels() {
        return routeViewModels;
    }

    public void setRouteViewModels(List<FlightDetailRouteViewModel> routeViewModels) {
        this.routeViewModels = routeViewModels;
    }

    public String getDepartureCityCode() {
        return departureCityCode;
    }

    public void setDepartureCityCode(String departureCityCode) {
        this.departureCityCode = departureCityCode;
    }

    public String getArrivalCityCode() {
        return arrivalCityCode;
    }

    public void setArrivalCityCode(String arrivalCityCode) {
        this.arrivalCityCode = arrivalCityCode;
    }

    @Override
    public int type(FlightDetailOrderTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
