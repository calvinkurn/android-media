package com.tokopedia.flight.orderlist.domain.model;

import com.tokopedia.flight.orderlist.data.cloud.entity.PaymentInfoEntity;

import java.util.List;

/**
 * @author by alvarisi on 12/11/17.
 */

public class FlightOrder {
    private String id;
    private int status;
    private String statusString;
    private String createTime;
    private String email;
    private String telp;
    private String totalAdult;
    private int totalAdultNumeric;
    private String totalChild;
    private int totalChildNumeric;
    private String totalInfant;
    private int totalInfantNumeric;
    private String currency;
    private String pdf;
    private List<FlightOrderJourney> journeys;
    private List<FlightOrderPassengerViewModel> passengerViewModels;
    private PaymentInfoEntity payment;

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

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public PaymentInfoEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentInfoEntity payment) {
        this.payment = payment;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
