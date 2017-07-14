package com.tokopedia.seller.common.domain.model;

import java.util.Date;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class DatePickerDomainModel {

    private Date startDate;
    private Date endDate;
    private Date comparedStartDate;
    private Date comparedEndDate;
    private int datePickertype;
    private int datePickerSelection;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getComparedStartDate() {
        return comparedStartDate;
    }

    public void setComparedStartDate(Date comparedStartDate) {
        this.comparedStartDate = comparedStartDate;
    }

    public Date getComparedEndDate() {
        return comparedEndDate;
    }

    public void setComparedEndDate(Date comparedEndDate) {
        this.comparedEndDate = comparedEndDate;
    }

    public int getDatePickertype() {
        return datePickertype;
    }

    public void setDatePickertype(int datePickertype) {
        this.datePickertype = datePickertype;
    }

    public int getDatePickerSelection() {
        return datePickerSelection;
    }

    public void setDatePickerSelection(int datePickerSelection) {
        this.datePickerSelection = datePickerSelection;
    }
}
