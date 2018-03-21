package com.tokopedia.events.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Seat {

    @SerializedName("areaId")
    @Expose
    private String areaId;
    @SerializedName("no")
    @Expose
    private int no;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("actualSeat")
    @Expose
    private int actualSeat;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public int getActualSeat() {
        return actualSeat;
    }

    public void setActualSeat(int actualSeat) {
        this.actualSeat = actualSeat;
    }

}
