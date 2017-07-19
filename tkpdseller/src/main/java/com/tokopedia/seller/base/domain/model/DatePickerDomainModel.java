package com.tokopedia.seller.base.domain.model;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class DatePickerDomainModel {

    private long startDate;
    private long endDate;
    private int datePickerType;
    private int datePickerSelection;
    private boolean compareDate;

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

    public boolean isCompareDate() {
        return compareDate;
    }

    public void setCompareDate(boolean compareDate) {
        this.compareDate = compareDate;
    }

    public DatePickerDomainModel() {

    }

    public DatePickerDomainModel(DatePickerDomainModel datePickerDomainModel) {
        this.startDate = datePickerDomainModel.getStartDate();
        this.endDate = datePickerDomainModel.getEndDate();
        this.compareDate = datePickerDomainModel.isCompareDate();
        this.datePickerType = datePickerDomainModel.getDatePickerType();
        this.datePickerSelection = datePickerDomainModel.getDatePickerSelection();
    }
}
