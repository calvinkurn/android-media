package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.AttachmentViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 18/08/17.
 */

public class ResultViewModel implements Parcelable {

    public static final String PARAM_COMPLAINT = "complaints";
    public static final String PARAM_SOLUTION = "solution";
    public static final String PARAM_ID = "id";
    public static final String PARAM_REFUND = "refundAmount";
    public static final String PARAM_ATTACHMENT = "attachment";
    public static final String PARAM_COUNT = "count";
    public static final String PARAM_CONVERSATION = "conversation";
    public static final String PARAM_RESOLUTION_ID = "resolutionID";
    public List<ComplaintResult> complaints = new ArrayList<>();
    public int solution;
    public String solutionName;
    public int refundAmount;
    public ProblemMessageResult message;
    public int attachmentCount;
    public String orderId;
    public boolean isAttachmentRequired;
    public String resolutionId;
    public List<AttachmentViewModel> attachmentList = new ArrayList<>();

    public ResultViewModel() {
        message = new ProblemMessageResult();
        clearSolution();
        clearAttachment();
    }

    public void clearSolution() {
        solutionName = "";
        solution = 0;
        refundAmount = 0;
        isAttachmentRequired = false;
    }

    public void clearAttachment() {
        attachmentCount = 0;
        attachmentList = new ArrayList<>();
        message = new ProblemMessageResult();
    }

    public JsonObject writeToJson() {
        JsonObject object = new JsonObject();
        try {
            if (complaints.size() != 0) {
                object.add(PARAM_COMPLAINT, getProblemArray());
            }
            if (solution != 0) {
                JsonObject solutionObject = new JsonObject();
                solutionObject.addProperty(PARAM_ID, solution);
                object.add(PARAM_SOLUTION, solutionObject);
            }
            if (refundAmount != 0) {
                object.addProperty(PARAM_REFUND, refundAmount);
            }
            if (attachmentCount != 0) {
                JsonObject attachmentObject = new JsonObject();
                attachmentObject.addProperty(PARAM_COUNT, attachmentCount);
                object.add(PARAM_ATTACHMENT, attachmentObject);
            }
            if (message != null) {
                if (!message.remark.equals("")) {
                    object.add(PARAM_CONVERSATION, message.writeToJson());
                }
            }
            if (resolutionId != null) {
                object.addProperty(PARAM_RESOLUTION_ID, Integer.valueOf(resolutionId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public JsonArray getProblemArray() {
        JsonArray complaintArray = new JsonArray();
        for (ComplaintResult complaint : complaints) {
            complaintArray.add(complaint.writeToJson());
        }
        return complaintArray;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.complaints);
        dest.writeInt(this.solution);
        dest.writeString(this.solutionName);
        dest.writeInt(this.refundAmount);
        dest.writeParcelable(this.message, flags);
        dest.writeInt(this.attachmentCount);
        dest.writeString(this.orderId);
        dest.writeByte(this.isAttachmentRequired ? (byte) 1 : (byte) 0);
        dest.writeString(this.resolutionId);
        dest.writeTypedList(this.attachmentList);
    }

    protected ResultViewModel(Parcel in) {
        this.complaints = in.createTypedArrayList(ComplaintResult.CREATOR);
        this.solution = in.readInt();
        this.solutionName = in.readString();
        this.refundAmount = in.readInt();
        this.message = in.readParcelable(ProblemMessageResult.class.getClassLoader());
        this.attachmentCount = in.readInt();
        this.orderId = in.readString();
        this.isAttachmentRequired = in.readByte() != 0;
        this.resolutionId = in.readString();
        this.attachmentList = in.createTypedArrayList(AttachmentViewModel.CREATOR);
    }

    public static final Creator<ResultViewModel> CREATOR = new Creator<ResultViewModel>() {
        @Override
        public ResultViewModel createFromParcel(Parcel source) {
            return new ResultViewModel(source);
        }

        @Override
        public ResultViewModel[] newArray(int size) {
            return new ResultViewModel[size];
        }
    };
}
