
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DebugLockingStatus {

    @SerializedName("PreviousStatus")
    @Expose
    private int previousStatus;
    @SerializedName("NextStatus")
    @Expose
    private int nextStatus;
    @SerializedName("DayLeft")
    @Expose
    private int dayLeft;

    public int getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(int previousStatus) {
        this.previousStatus = previousStatus;
    }

    public int getNextStatus() {
        return nextStatus;
    }

    public void setNextStatus(int nextStatus) {
        this.nextStatus = nextStatus;
    }

    public int getDayLeft() {
        return dayLeft;
    }

    public void setDayLeft(int dayLeft) {
        this.dayLeft = dayLeft;
    }

}
