package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 14/09/17.
 */

public class EditAppealSolutionModel implements Parcelable {
    public boolean isChatReso;
    public boolean isEdit;
    public String resolutionId;
    public boolean isSeller;
    public int solution;
    public String name;
    public String solutionName;
    public int refundAmount;

    public boolean isChatReso() {
        return isChatReso;
    }

    public void setChatReso(boolean chatReso) {
        isChatReso = chatReso;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public String getResolutionId() {
        return resolutionId;
    }

    public void setResolutionId(String resolutionId) {
        this.resolutionId = resolutionId;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }

    public int getSolution() {
        return solution;
    }

    public void setSolution(int solution) {
        this.solution = solution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public EditAppealSolutionModel(boolean isEdit, String resolutionId, boolean isSeller, boolean isChatReso) {
        this.isEdit = isEdit;
        this.resolutionId = resolutionId;
        this.isSeller = isSeller;
        this.isChatReso = isChatReso;
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
        dest.writeString(this.name);
        dest.writeString(this.solutionName);
        dest.writeInt(this.refundAmount);
    }

    protected EditAppealSolutionModel(Parcel in) {
        this.isEdit = in.readByte() != 0;
        this.resolutionId = in.readString();
        this.isSeller = in.readByte() != 0;
        this.solution = in.readInt();
        this.name = in.readString();
        this.solutionName = in.readString();
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
