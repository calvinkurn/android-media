package com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class GroupAddress implements Parcelable {
    private boolean isError;
    private String errorMessage;
    private boolean isWarning;
    private String warningMessage;

    private UserAddress userAddress;
    private List<GroupShop> groupShop = new ArrayList<>();

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public UserAddress getUserAddress() {
        return userAddress;
    }

    public List<GroupShop> getGroupShop() {
        return groupShop;
    }


    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public void setGroupShop(List<GroupShop> groupShop) {
        this.groupShop = groupShop;
    }

    public GroupAddress() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
        dest.writeByte(this.isWarning ? (byte) 1 : (byte) 0);
        dest.writeString(this.warningMessage);
        dest.writeParcelable(this.userAddress, flags);
        dest.writeTypedList(this.groupShop);
    }

    protected GroupAddress(Parcel in) {
        this.isError = in.readByte() != 0;
        this.errorMessage = in.readString();
        this.isWarning = in.readByte() != 0;
        this.warningMessage = in.readString();
        this.userAddress = in.readParcelable(UserAddress.class.getClassLoader());
        this.groupShop = in.createTypedArrayList(GroupShop.CREATOR);
    }

    public static final Creator<GroupAddress> CREATOR = new Creator<GroupAddress>() {
        @Override
        public GroupAddress createFromParcel(Parcel source) {
            return new GroupAddress(source);
        }

        @Override
        public GroupAddress[] newArray(int size) {
            return new GroupAddress[size];
        }
    };
}
