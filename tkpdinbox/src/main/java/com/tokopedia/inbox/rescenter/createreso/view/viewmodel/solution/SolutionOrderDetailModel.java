package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionOrderDetailModel implements Parcelable {

    private int id;
    private boolean freeReturn;

    public SolutionOrderDetailModel(int id, boolean freeReturn) {
        this.id = id;
        this.freeReturn = freeReturn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(boolean freeReturn) {
        this.freeReturn = freeReturn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.freeReturn ? (byte) 1 : (byte) 0);
        dest.writeInt(this.id);
    }

    protected SolutionOrderDetailModel(Parcel in) {
        this.freeReturn = in.readByte() != 0;
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<SolutionOrderDetailModel> CREATOR = new Parcelable.Creator<SolutionOrderDetailModel>() {
        @Override
        public SolutionOrderDetailModel createFromParcel(Parcel source) {
            return new SolutionOrderDetailModel(source);
        }

        @Override
        public SolutionOrderDetailModel[] newArray(int size) {
            return new SolutionOrderDetailModel[size];
        }
    };
}
