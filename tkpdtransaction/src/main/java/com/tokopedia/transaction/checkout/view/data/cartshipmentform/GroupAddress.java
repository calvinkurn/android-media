package com.tokopedia.transaction.checkout.view.data.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class GroupAddress implements Parcelable {
    private List<String> errors = new ArrayList<>();
    private UserAddress userAddress;

    private List<GroupShop> groupShop = new ArrayList<>();

    public List<String> getErrors() {
        return errors;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public List<GroupShop> getGroupShop() {
        return groupShop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.errors);
        dest.writeParcelable(this.userAddress, flags);
        dest.writeTypedList(this.groupShop);
    }

    public GroupAddress() {
    }

    protected GroupAddress(Parcel in) {
        this.errors = in.createStringArrayList();
        this.userAddress = in.readParcelable(UserAddress.class.getClassLoader());
        this.groupShop = in.createTypedArrayList(GroupShop.CREATOR);
    }

    public static final Parcelable.Creator<GroupAddress> CREATOR = new Parcelable.Creator<GroupAddress>() {
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
