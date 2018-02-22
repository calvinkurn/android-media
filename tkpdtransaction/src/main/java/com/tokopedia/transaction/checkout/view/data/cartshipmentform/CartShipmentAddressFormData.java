package com.tokopedia.transaction.checkout.view.data.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class CartShipmentAddressFormData implements Parcelable {

    private List<String> errors = new ArrayList<>();
    private int errorCode;
    private boolean isMultiple;
    private List<GroupAddress> groupAddress = new ArrayList<>();
    private String keroToken;
    private String keroDiscomToken;
    private int keroUnixTime;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    public List<GroupAddress> getGroupAddress() {
        return groupAddress;
    }

    public void setGroupAddress(List<GroupAddress> groupAddress) {
        this.groupAddress = groupAddress;
    }

    public String getKeroToken() {
        return keroToken;
    }

    public void setKeroToken(String keroToken) {
        this.keroToken = keroToken;
    }

    public String getKeroDiscomToken() {
        return keroDiscomToken;
    }

    public void setKeroDiscomToken(String keroDiscomToken) {
        this.keroDiscomToken = keroDiscomToken;
    }

    public int getKeroUnixTime() {
        return keroUnixTime;
    }

    public void setKeroUnixTime(int keroUnixTime) {
        this.keroUnixTime = keroUnixTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.errors);
        dest.writeInt(this.errorCode);
        dest.writeByte(this.isMultiple ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.groupAddress);
        dest.writeString(this.keroToken);
        dest.writeString(this.keroDiscomToken);
        dest.writeInt(this.keroUnixTime);
    }

    public CartShipmentAddressFormData() {
    }

    protected CartShipmentAddressFormData(Parcel in) {
        this.errors = in.createStringArrayList();
        this.errorCode = in.readInt();
        this.isMultiple = in.readByte() != 0;
        this.groupAddress = in.createTypedArrayList(GroupAddress.CREATOR);
        this.keroToken = in.readString();
        this.keroDiscomToken = in.readString();
        this.keroUnixTime = in.readInt();
    }

    public static final Parcelable.Creator<CartShipmentAddressFormData> CREATOR = new Parcelable.Creator<CartShipmentAddressFormData>() {
        @Override
        public CartShipmentAddressFormData createFromParcel(Parcel source) {
            return new CartShipmentAddressFormData(source);
        }

        @Override
        public CartShipmentAddressFormData[] newArray(int size) {
            return new CartShipmentAddressFormData[size];
        }
    };
}
