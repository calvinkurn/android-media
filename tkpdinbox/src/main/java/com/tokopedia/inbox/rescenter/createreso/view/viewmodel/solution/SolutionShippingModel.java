package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionShippingModel implements Parcelable {

    private int fee;
    private boolean checked;

    public SolutionShippingModel(int fee, boolean checked) {
        this.fee = fee;
        this.checked = checked;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.fee);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
    }

    protected SolutionShippingModel(Parcel in) {
        this.fee = in.readInt();
        this.checked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SolutionShippingModel> CREATOR = new Parcelable.Creator<SolutionShippingModel>() {
        @Override
        public SolutionShippingModel createFromParcel(Parcel source) {
            return new SolutionShippingModel(source);
        }

        @Override
        public SolutionShippingModel[] newArray(int size) {
            return new SolutionShippingModel[size];
        }
    };
}
