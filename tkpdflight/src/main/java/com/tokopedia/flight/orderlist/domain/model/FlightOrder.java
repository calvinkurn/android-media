package com.tokopedia.flight.orderlist.domain.model;

import java.util.List;

/**
 * @author by alvarisi on 12/11/17.
 */

public class FlightOrder {
    protected String id;
    protected int status;
    protected String createTime;
    protected String email;
    protected String telp;
    protected String totalAdult;
    protected int totalAdultNumeric;
    protected String totalChild;
    protected int totalChildNumeric;
    protected String totalInfant;
    protected int totalInfantNumeric;
    protected String currency;
    protected List<FlightOrderJourney> journeys;
    protected List<FlightOrderPassengerViewModel> passengerViewModels;

    public FlightOrder() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getTotalAdult() {
        return totalAdult;
    }

    public void setTotalAdult(String totalAdult) {
        this.totalAdult = totalAdult;
    }

    public int getTotalAdultNumeric() {
        return totalAdultNumeric;
    }

    public void setTotalAdultNumeric(int totalAdultNumeric) {
        this.totalAdultNumeric = totalAdultNumeric;
    }

    public String getTotalChild() {
        return totalChild;
    }

    public void setTotalChild(String totalChild) {
        this.totalChild = totalChild;
    }

    public int getTotalChildNumeric() {
        return totalChildNumeric;
    }

    public void setTotalChildNumeric(int totalChildNumeric) {
        this.totalChildNumeric = totalChildNumeric;
    }

    public String getTotalInfant() {
        return totalInfant;
    }

    public void setTotalInfant(String totalInfant) {
        this.totalInfant = totalInfant;
    }

    public int getTotalInfantNumeric() {
        return totalInfantNumeric;
    }

    public void setTotalInfantNumeric(int totalInfantNumeric) {
        this.totalInfantNumeric = totalInfantNumeric;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<FlightOrderJourney> getJourneys() {
        return journeys;
    }

    public void setJourneys(List<FlightOrderJourney> journeys) {
        this.journeys = journeys;
    }

    public List<FlightOrderPassengerViewModel> getPassengerViewModels() {
        return passengerViewModels;
    }

    public void setPassengerViewModels(List<FlightOrderPassengerViewModel> passengerViewModels) {
        this.passengerViewModels = passengerViewModels;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
