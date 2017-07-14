package com.tokopedia.seller.common.domain.model;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class DatePickerDomainModel {

    private String startDate;
    private String endDate;
    private String comparedStartDate;
    private String comparedEndDate;
    private int datePickerType;
    private int datePickerSelection;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getComparedStartDate() {
        return comparedStartDate;
    }

    public void setComparedStartDate(String comparedStartDate) {
        this.comparedStartDate = comparedStartDate;
    }

    public String getComparedEndDate() {
        return comparedEndDate;
    }

    public void setComparedEndDate(String comparedEndDate) {
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
}
