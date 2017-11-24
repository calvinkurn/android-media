
package com.tokopedia.events.view.viewmodel;

public class ScheduleViewModel {


    private String title;
    private Integer startDate;
    private Integer endDate;
    private String tnc;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStartDate() {
        return startDate;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public Integer getEndDate() {
        return endDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }


}
