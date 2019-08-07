package com.tokopedia.seller.purchase.detail.model.detail.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Fajar Ulin Nuha on 12/10/18.
 */
public class BookingCodeData implements Parcelable {

    private String bookingCode;
    private String barcodeType;
    private List<String> message = null;

    public BookingCodeData(String bookingCode, String barcodeType, List<String> message) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bookingCode);
        dest.writeString(this.barcodeType);
        dest.writeStringList(this.message);
    }

    protected BookingCodeData(Parcel in) {
        this.bookingCode = in.readString();
        this.barcodeType = in.readString();
        this.message = in.createStringArrayList();
    }

    public static final Parcelable.Creator<BookingCodeData> CREATOR = new Parcelable.Creator<BookingCodeData>() {
        @Override
        public BookingCodeData createFromParcel(Parcel source) {
            return new BookingCodeData(source);
        }

        @Override
        public BookingCodeData[] newArray(int size) {
            return new BookingCodeData[size];
        }
    };
}
