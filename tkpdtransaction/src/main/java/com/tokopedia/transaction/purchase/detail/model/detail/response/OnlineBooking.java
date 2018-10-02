package com.tokopedia.transaction.purchase.detail.model.detail.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnlineBooking implements Parcelable {

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

    public OnlineBooking(Parcel in) {
        bookingCode = in.readString();
        barcodeType = in.readString();
        message = in.createStringArrayList();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(bookingCode);
        out.writeString(barcodeType);
        out.writeStringList(message);
    }

    public static final Parcelable.Creator<OnlineBooking> CREATOR
            = new Parcelable.Creator<OnlineBooking>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public OnlineBooking createFromParcel(Parcel in) {
            return new OnlineBooking(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public OnlineBooking[] newArray(int size) {
            return new OnlineBooking[size];
        }
    };
}
