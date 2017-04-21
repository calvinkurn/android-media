package com.tokopedia.seller.reputation.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.topads.view.model.TypeBasedModel;

/**
 * Created by normansyahputa on 3/30/17.
 */

public class EmptyListModel extends TypeBasedModel implements Parcelable {
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
        super(TYPE);
    }

    protected EmptyListModel(Parcel in) {
        this();
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
}
