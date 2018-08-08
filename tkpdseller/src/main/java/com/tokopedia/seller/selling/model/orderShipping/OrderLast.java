
package com.tokopedia.seller.selling.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class OrderLast {

    @SerializedName("last_order_id")
    @Expose
    Integer lastOrderId;
    @SerializedName("last_shipment_id")
    @Expose
    String lastShipmentId;
    @SerializedName("last_est_shipping_left")
    @Expose
    Integer lastEstShippingLeft;
    @SerializedName("last_order_status")
    @Expose
    String lastOrderStatus;
    @SerializedName("last_status_date")
    @Expose
    String lastStatusDate;
    @SerializedName("last_pod_code")
    @Expose
    Integer lastPodCode;
    @SerializedName("last_pod_desc")
    @Expose
    String lastPodDesc;
    @SerializedName("last_shipping_ref_num")
    @Expose
    String lastShippingRefNum;
    @SerializedName("last_pod_receiver")
    @Expose
    Integer lastPodReceiver;
    @SerializedName("last_comments")
    @Expose
    String lastComments;
    @SerializedName("last_buyer_status")
    @Expose
    String lastBuyerStatus;
    @SerializedName("last_status_date_wib")
    @Expose
    String lastStatusDateWib;
    @SerializedName("last_seller_status")
    @Expose
    String lastSellerStatus;

    /**
     * 
     * @return
     *     The lastOrderId
     */
    public Integer getLastOrderId() {
        return lastOrderId;
    }

    /**
     * 
     * @param lastOrderId
     *     The last_order_id
     */
    public void setLastOrderId(Integer lastOrderId) {
        this.lastOrderId = lastOrderId;
    }

    /**
     * 
     * @return
     *     The lastShipmentId
     */
    public String getLastShipmentId() {
        return lastShipmentId;
    }

    /**
     * 
     * @param lastShipmentId
     *     The last_shipment_id
     */
    public void setLastShipmentId(String lastShipmentId) {
        this.lastShipmentId = lastShipmentId;
    }

    /**
     * 
     * @return
     *     The lastEstShippingLeft
     */
    public Integer getLastEstShippingLeft() {
        return lastEstShippingLeft;
    }

    /**
     * 
     * @param lastEstShippingLeft
     *     The last_est_shipping_left
     */
    public void setLastEstShippingLeft(Integer lastEstShippingLeft) {
        this.lastEstShippingLeft = lastEstShippingLeft;
    }

    /**
     * 
     * @return
     *     The lastOrderStatus
     */
    public String getLastOrderStatus() {
        return lastOrderStatus;
    }

    /**
     * 
     * @param lastOrderStatus
     *     The last_order_status
     */
    public void setLastOrderStatus(String lastOrderStatus) {
        this.lastOrderStatus = lastOrderStatus;
    }

    /**
     * 
     * @return
     *     The lastStatusDate
     */
    public String getLastStatusDate() {
        return lastStatusDate;
    }

    /**
     * 
     * @param lastStatusDate
     *     The last_status_date
     */
    public void setLastStatusDate(String lastStatusDate) {
        this.lastStatusDate = lastStatusDate;
    }

    /**
     * 
     * @return
     *     The lastPodCode
     */
    public Integer getLastPodCode() {
        return lastPodCode;
    }

    /**
     * 
     * @param lastPodCode
     *     The last_pod_code
     */
    public void setLastPodCode(Integer lastPodCode) {
        this.lastPodCode = lastPodCode;
    }

    /**
     * 
     * @return
     *     The lastPodDesc
     */
    public String getLastPodDesc() {
        return lastPodDesc;
    }

    /**
     * 
     * @param lastPodDesc
     *     The last_pod_desc
     */
    public void setLastPodDesc(String lastPodDesc) {
        this.lastPodDesc = lastPodDesc;
    }

    /**
     * 
     * @return
     *     The lastShippingRefNum
     */
    public String getLastShippingRefNum() {
        return lastShippingRefNum;
    }

    /**
     * 
     * @param lastShippingRefNum
     *     The last_shipping_ref_num
     */
    public void setLastShippingRefNum(String lastShippingRefNum) {
        this.lastShippingRefNum = lastShippingRefNum;
    }

    /**
     * 
     * @return
     *     The lastPodReceiver
     */
    public Integer getLastPodReceiver() {
        return lastPodReceiver;
    }

    /**
     * 
     * @param lastPodReceiver
     *     The last_pod_receiver
     */
    public void setLastPodReceiver(Integer lastPodReceiver) {
        this.lastPodReceiver = lastPodReceiver;
    }

    /**
     * 
     * @return
     *     The lastComments
     */
    public String getLastComments() {
        return lastComments;
    }

    /**
     * 
     * @param lastComments
     *     The last_comments
     */
    public void setLastComments(String lastComments) {
        this.lastComments = lastComments;
    }

    /**
     * 
     * @return
     *     The lastBuyerStatus
     */
    public String getLastBuyerStatus() {
        return lastBuyerStatus;
    }

    /**
     * 
     * @param lastBuyerStatus
     *     The last_buyer_status
     */
    public void setLastBuyerStatus(String lastBuyerStatus) {
        this.lastBuyerStatus = lastBuyerStatus;
    }

    /**
     * 
     * @return
     *     The lastStatusDateWib
     */
    public String getLastStatusDateWib() {
        return lastStatusDateWib;
    }

    /**
     * 
     * @param lastStatusDateWib
     *     The last_status_date_wib
     */
    public void setLastStatusDateWib(String lastStatusDateWib) {
        this.lastStatusDateWib = lastStatusDateWib;
    }

    /**
     * 
     * @return
     *     The lastSellerStatus
     */
    public String getLastSellerStatus() {
        return lastSellerStatus;
    }

    /**
     * 
     * @param lastSellerStatus
     *     The last_seller_status
     */
    public void setLastSellerStatus(String lastSellerStatus) {
        this.lastSellerStatus = lastSellerStatus;
    }

}
