package com.tokopedia.seller.reputation.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.base.list.seller.common.util.ItemType;

/**
 * Created by normansyahputa on 3/30/17.
 */

public class EmptyListModel implements Parcelable, ItemType {
    public static final int TYPE = 128912;
    public static final Parcelable.Creator<EmptyListModel> CREATOR = new Parcelable.Creator<EmptyListModel>() {
        @Override
        public EmptyListModel createFromParcel(Parcel source) {
            return new EmptyListModel(source);
        }

        @Override
        public EmptyListModel[] newArray(int size) {
            return new EmptyListModel[size];
        }
    };
    SetDateHeaderModel setDateHeaderModel;
    private boolean isEmptyShop;

    public EmptyListModel() {
    }

    protected EmptyListModel(Parcel in) {
        this.setDateHeaderModel = in.readParcelable(SetDateHeaderModel.class.getClassLoader());
        this.isEmptyShop = in.readByte() != 0;
    }

    public SetDateHeaderModel getSetDateHeaderModel() {
        return setDateHeaderModel;
    }

    public void setSetDateHeaderModel(SetDateHeaderModel setDateHeaderModel) {
        this.setDateHeaderModel = setDateHeaderModel;
    }

    public boolean isEmptyShop() {
        return isEmptyShop;
    }

    public void setEmptyShop(boolean emptyShop) {
        isEmptyShop = emptyShop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.setDateHeaderModel, flags);
        dest.writeByte(this.isEmptyShop ? (byte) 1 : (byte) 0);
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
