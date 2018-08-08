
package com.tokopedia.seller.selling.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class OrderDeadline {

    @SerializedName("deadline_finish_day_left")
    @Expose
    String deadlineFinishDayLeft;
    @SerializedName("deadline_proccess_hour_left")
    @Expose
    String deadlineProcessHourLeft;
    @SerializedName("deadline_process")
    @Expose
    String deadLineProcess;
    @SerializedName("deadline_process_day_left")
    @Expose
    String deadlineProcessDayLeft;
    @SerializedName("deadline_po_process_day_left")
    @Expose
    String deadlinePoProcessDayLeft;
    @SerializedName("deadline_finish_date")
    @Expose
    String deadlineFinishDate;
    @SerializedName("deadline_shipping_day_left")
    @Expose
    String deadlineShippingDayLeft;
    @SerializedName("deadline_shipping_hour_left")
    @Expose
    String deadlineShippingHourLeft;
    @SerializedName("deadline_shipping")
    @Expose
    String deadlineShipping;
    @SerializedName("deadline_finish_hour_left")
    @Expose
    String deadlineFinishHourLeft;
    @SerializedName("deadline_color")
    @Expose
    String deadlineColor;

    /**
     * 
     * @return
     *     The deadlineFinishDayLeft
     */
    public String getDeadlineFinishDayLeft() {
        return deadlineFinishDayLeft;
    }

    /**
     * 
     * @param deadlineFinishDayLeft
     *     The deadline_finish_day_left
     */
    public void setDeadlineFinishDayLeft(String deadlineFinishDayLeft) {
        this.deadlineFinishDayLeft = deadlineFinishDayLeft;
    }

    /**
     * 
     * @return
     *     The deadlineProcessDayLeft
     */
    public String getDeadlineProcessDayLeft() {
        return deadlineProcessDayLeft;
    }

    /**
     * 
     * @param deadlineProcessDayLeft
     *     The deadline_process_day_left
     */
    public void setDeadlineProcessDayLeft(String deadlineProcessDayLeft) {
        this.deadlineProcessDayLeft = deadlineProcessDayLeft;
    }

    /**
     * 
     * @return
     *     The deadlinePoProcessDayLeft
     */
    public String getDeadlinePoProcessDayLeft() {
        return deadlinePoProcessDayLeft;
    }

    /**
     * 
     * @param deadlinePoProcessDayLeft
     *     The deadline_po_process_day_left
     */
    public void setDeadlinePoProcessDayLeft(String deadlinePoProcessDayLeft) {
        this.deadlinePoProcessDayLeft = deadlinePoProcessDayLeft;
    }

    /**
     * 
     * @return
     *     The deadlineFinishDate
     */
    public String getDeadlineFinishDate() {
        return deadlineFinishDate;
    }

    /**
     * 
     * @param deadlineFinishDate
     *     The deadline_finish_date
     */
    public void setDeadlineFinishDate(String deadlineFinishDate) {
        this.deadlineFinishDate = deadlineFinishDate;
    }

    /**
     * 
     * @return
     *     The deadlineShippingDayLeft
     */
    public String getDeadlineShippingDayLeft() {
        return deadlineShippingDayLeft;
    }

    /**
     * 
     * @param deadlineShippingDayLeft
     *     The deadline_shipping_day_left
     */
    public void setDeadlineShippingDayLeft(String deadlineShippingDayLeft) {
        this.deadlineShippingDayLeft = deadlineShippingDayLeft;
    }

    public String getDeadlineShippingHourLeft() {
        return deadlineShippingHourLeft;
    }

    public void setDeadlineShippingHourLeft(String deadlineShippingHourLeft) {
        this.deadlineShippingHourLeft = deadlineShippingHourLeft;
    }

    public String getDeadlineShipping() {
        return deadlineShipping;
    }

    public void setDeadlineShipping(String deadlineShipping) {
        this.deadlineShipping = deadlineShipping;
    }

    public String getDeadlineFinishHourLeft() {
        return deadlineFinishHourLeft;
    }

    public void setDeadlineFinishHourLeft(String deadlineFinishHourLeft) {
        this.deadlineFinishHourLeft = deadlineFinishHourLeft;
    }

    public String getDeadlineColor() {
        return deadlineColor;
    }

    public void setDeadlineColor(String deadlineColor) {
        this.deadlineColor = deadlineColor;
    }

    public String getDeadlineProcessHourLeft() {
        return deadlineProcessHourLeft;
    }

    public void setDeadlineProcessHourLeft(String deadlineProcessHourLeft) {
        this.deadlineProcessHourLeft = deadlineProcessHourLeft;
    }

    public String getDeadLineProcess() {
        return deadLineProcess;
    }

    public void setDeadLineProcess(String deadLineProcess) {
        this.deadLineProcess = deadLineProcess;
    }
}
