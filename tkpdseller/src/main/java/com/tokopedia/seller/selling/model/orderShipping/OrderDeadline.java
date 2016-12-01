
package com.tokopedia.seller.selling.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class OrderDeadline {

    @SerializedName("deadline_finish_day_left")
    @Expose
    String deadlineFinishDayLeft;
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

}
