
package com.tokopedia.seller.transaction.neworder.data.source.cloud.model.neworder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDeadline {

    @SerializedName("deadline_process_day_left")
    @Expose
    private int deadlineProcessDayLeft;
    @SerializedName("deadline_process_hour_left")
    @Expose
    private int deadlineProcessHourLeft;
    @SerializedName("deadline_process_minute_left")
    @Expose
    private int deadlineProcessMinuteLeft;
    @SerializedName("deadline_process")
    @Expose
    private String deadlineProcess;
    @SerializedName("deadline_po_process_day_left")
    @Expose
    private int deadlinePoProcessDayLeft;
    @SerializedName("deadline_shipping_day_left")
    @Expose
    private int deadlineShippingDayLeft;
    @SerializedName("deadline_shipping_hour_left")
    @Expose
    private int deadlineShippingHourLeft;
    @SerializedName("deadline_shipping_minute_left")
    @Expose
    private int deadlineShippingMinuteLeft;
    @SerializedName("deadline_shipping")
    @Expose
    private String deadlineShipping;
    @SerializedName("deadline_finish_day_left")
    @Expose
    private int deadlineFinishDayLeft;
    @SerializedName("deadline_finish_hour_left")
    @Expose
    private int deadlineFinishHourLeft;
    @SerializedName("deadline_finish_minute_left")
    @Expose
    private int deadlineFinishMinuteLeft;
    @SerializedName("deadline_finish_date")
    @Expose
    private int deadlineFinishDate;
    @SerializedName("deadline_color")
    @Expose
    private String deadlineColor;

    public int getDeadlineProcessDayLeft() {
        return deadlineProcessDayLeft;
    }

    public void setDeadlineProcessDayLeft(int deadlineProcessDayLeft) {
        this.deadlineProcessDayLeft = deadlineProcessDayLeft;
    }

    public int getDeadlineProcessHourLeft() {
        return deadlineProcessHourLeft;
    }

    public void setDeadlineProcessHourLeft(int deadlineProcessHourLeft) {
        this.deadlineProcessHourLeft = deadlineProcessHourLeft;
    }

    public int getDeadlineProcessMinuteLeft() {
        return deadlineProcessMinuteLeft;
    }

    public void setDeadlineProcessMinuteLeft(int deadlineProcessMinuteLeft) {
        this.deadlineProcessMinuteLeft = deadlineProcessMinuteLeft;
    }

    public String getDeadlineProcess() {
        return deadlineProcess;
    }

    public void setDeadlineProcess(String deadlineProcess) {
        this.deadlineProcess = deadlineProcess;
    }

    public int getDeadlinePoProcessDayLeft() {
        return deadlinePoProcessDayLeft;
    }

    public void setDeadlinePoProcessDayLeft(int deadlinePoProcessDayLeft) {
        this.deadlinePoProcessDayLeft = deadlinePoProcessDayLeft;
    }

    public int getDeadlineShippingDayLeft() {
        return deadlineShippingDayLeft;
    }

    public void setDeadlineShippingDayLeft(int deadlineShippingDayLeft) {
        this.deadlineShippingDayLeft = deadlineShippingDayLeft;
    }

    public int getDeadlineShippingHourLeft() {
        return deadlineShippingHourLeft;
    }

    public void setDeadlineShippingHourLeft(int deadlineShippingHourLeft) {
        this.deadlineShippingHourLeft = deadlineShippingHourLeft;
    }

    public int getDeadlineShippingMinuteLeft() {
        return deadlineShippingMinuteLeft;
    }

    public void setDeadlineShippingMinuteLeft(int deadlineShippingMinuteLeft) {
        this.deadlineShippingMinuteLeft = deadlineShippingMinuteLeft;
    }

    public String getDeadlineShipping() {
        return deadlineShipping;
    }

    public void setDeadlineShipping(String deadlineShipping) {
        this.deadlineShipping = deadlineShipping;
    }

    public int getDeadlineFinishDayLeft() {
        return deadlineFinishDayLeft;
    }

    public void setDeadlineFinishDayLeft(int deadlineFinishDayLeft) {
        this.deadlineFinishDayLeft = deadlineFinishDayLeft;
    }

    public int getDeadlineFinishHourLeft() {
        return deadlineFinishHourLeft;
    }

    public void setDeadlineFinishHourLeft(int deadlineFinishHourLeft) {
        this.deadlineFinishHourLeft = deadlineFinishHourLeft;
    }

    public int getDeadlineFinishMinuteLeft() {
        return deadlineFinishMinuteLeft;
    }

    public void setDeadlineFinishMinuteLeft(int deadlineFinishMinuteLeft) {
        this.deadlineFinishMinuteLeft = deadlineFinishMinuteLeft;
    }

    public int getDeadlineFinishDate() {
        return deadlineFinishDate;
    }

    public void setDeadlineFinishDate(int deadlineFinishDate) {
        this.deadlineFinishDate = deadlineFinishDate;
    }

    public String getDeadlineColor() {
        return deadlineColor;
    }

    public void setDeadlineColor(String deadlineColor) {
        this.deadlineColor = deadlineColor;
    }

}
