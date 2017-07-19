package com.tokopedia.seller.base.domain.model;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class DatePickerDomainModel {

    private long startDate;
    private long endDate;
    private long comparedStartDate;
    private long comparedEndDate;
    private int datePickerType;
    private int datePickerSelection;

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public long getComparedStartDate() {
        return comparedStartDate;
    }

    public void setComparedStartDate(long comparedStartDate) {
        this.comparedStartDate = comparedStartDate;
    }

    public long getComparedEndDate() {
        return comparedEndDate;
    }

    public void setComparedEndDate(long comparedEndDate) {
        this.comparedEndDate = comparedEndDate;
    }

    public int getDatePickerType() {
        return datePickerType;
    }

    public void setDatePickerType(int datePickerType) {
        this.datePickerType = datePickerType;
    }

    public int getDatePickerSelection() {
        return datePickerSelection;
    }

    public void setDatePickerSelection(int datePickerSelection) {
        this.datePickerSelection = datePickerSelection;
    }

    public DatePickerDomainModel() {

    }

    public DatePickerDomainModel(DatePickerDomainModel datePickerDomainModel) {
        this.startDate = datePickerDomainModel.startDate;
        this.endDate = datePickerDomainModel.endDate;
        this.comparedStartDate = datePickerDomainModel.comparedStartDate;
        this.comparedEndDate = datePickerDomainModel.comparedEndDate;
        this.datePickerType = datePickerDomainModel.datePickerType;
        this.datePickerSelection = datePickerDomainModel.datePickerSelection;
    }
}
