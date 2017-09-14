package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 14/09/17.
 */

public class EditAppealSolutionModel implements Parcelable {
    public boolean isEdit;
    public String resolutionId;
    public boolean isSeller;
    public int solution;
    public String solutionName;
    public int refundAmount;

    public EditAppealSolutionModel(boolean isEdit, String resolutionId, boolean isSeller) {
        this.isEdit = isEdit;
        this.resolutionId = resolutionId;
        this.isSeller = isSeller;
        refundAmount = 0;
        solutionName = "";
        solution = 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isEdit ? (byte) 1 : (byte) 0);
        dest.writeString(this.resolutionId);
        dest.writeByte(this.isSeller ? (byte) 1 : (byte) 0);
        dest.writeInt(this.solution);
        dest.writeInt(this.refundAmount);
    }

    protected EditAppealSolutionModel(Parcel in) {
        this.isEdit = in.readByte() != 0;
        this.resolutionId = in.readString();
        this.isSeller = in.readByte() != 0;
        this.solution = in.readInt();
        this.refundAmount = in.readInt();
    }

    public static final Creator<EditAppealSolutionModel> CREATOR = new Creator<EditAppealSolutionModel>() {
        @Override
        public EditAppealSolutionModel createFromParcel(Parcel source) {
            return new EditAppealSolutionModel(source);
        }

        @Override
        public EditAppealSolutionModel[] newArray(int size) {
            return new EditAppealSolutionModel[size];
        }
    };
}
