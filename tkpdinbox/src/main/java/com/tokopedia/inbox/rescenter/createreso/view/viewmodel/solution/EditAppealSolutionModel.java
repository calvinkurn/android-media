package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ComplaintResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 14/09/17.
 */

public class EditAppealSolutionModel implements Parcelable {
    public static final String PARAM_COMPLAINT = "complaints";
    public static final String PARAM_SOLUTION = "solution";
    public static final String PARAM_ID = "id";
    public boolean isChatReso;
    public boolean isEdit;
    public String resolutionId;
    public boolean isSeller;
    public int solution;
    public String name;
    public String solutionName;
    public int refundAmount;
    public List<ComplaintResult> complaints = new ArrayList<>();

    public JsonObject writeToJson() {
        JsonObject object = new JsonObject();
        if (complaints != null) {
            JsonArray complaintArray = new JsonArray();
            for (ComplaintResult complaintResult : complaints) {
                if (complaintResult.problem.type != 1) {
                    complaintArray.add(complaintResult.writeToJson());
                } else if (complaintResult.isChecked) {
                    complaintArray.add(complaintResult.writeToJson());
                }
            }
            object.add(PARAM_COMPLAINT, complaintArray);
        }
        JsonObject solutionObject = new JsonObject();
        solutionObject.addProperty(PARAM_ID, solution);
        object.add(PARAM_SOLUTION, solutionObject);
        return object;
    }


    public List<ComplaintResult> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<ComplaintResult> complaints) {
        this.complaints = complaints;
    }

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
        dest.writeByte(this.isChatReso ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isEdit ? (byte) 1 : (byte) 0);
        dest.writeString(this.resolutionId);
        dest.writeByte(this.isSeller ? (byte) 1 : (byte) 0);
        dest.writeInt(this.solution);
        dest.writeString(this.name);
        dest.writeString(this.solutionName);
        dest.writeInt(this.refundAmount);
        dest.writeTypedList(this.complaints);
    }

    protected EditAppealSolutionModel(Parcel in) {
        this.isChatReso = in.readByte() != 0;
        this.isEdit = in.readByte() != 0;
        this.resolutionId = in.readString();
        this.isSeller = in.readByte() != 0;
        this.solution = in.readInt();
        this.name = in.readString();
        this.solutionName = in.readString();
        this.refundAmount = in.readInt();
        this.complaints = in.createTypedArrayList(ComplaintResult.CREATOR);
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
