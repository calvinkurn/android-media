package com.tokopedia.seller.purchase.detail.model.detail.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OnlineBooking {

    @SerializedName("booking_code")
    @Expose
    private String bookingCode;
    @SerializedName("barcode_type")
    @Expose
    private String barcodeType;
    @SerializedName("message")
    @Expose
    private List<String> message = null;

    public OnlineBooking(String bookingCode, String barcodeType, List<String> message) {
        this.bookingCode = bookingCode;
        this.barcodeType = barcodeType;
        this.message = message;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

}
