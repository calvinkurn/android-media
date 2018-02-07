package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.AttachmentViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 18/08/17.
 */

public class ResultViewModel implements Parcelable {

    public static final String PARAM_PROBLEM = "problem";
    public static final String PARAM_SOLUTION = "solution";
    public static final String PARAM_REFUND = "refundAmount";
    public static final String PARAM_ATTACHMENT = "attachmentCount";
    public static final String PARAM_MESSAGE = "message";
    public static final String PARAM_RESOLUTION_ID = "resolutionID";
    public List<ProblemResult> problem = new ArrayList<>();
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

    public JSONObject writeToJson() {
        JSONObject object = new JSONObject();
        try {
            if (problem.size() != 0) {
                object.put(PARAM_PROBLEM, getProblemArray());
            }
            if (solution != 0) {
                object.put(PARAM_SOLUTION, solution);
            }
            if (refundAmount != 0) {
                object.put(PARAM_REFUND, refundAmount);
            }
            if (attachmentCount != 0) {
                object.put(PARAM_ATTACHMENT, attachmentCount);
            }
            if (message != null) {
                if (!message.remark.equals("")) {
                    object.put(PARAM_MESSAGE, message.writeToJson());
                }
            }
            if (resolutionId != null) {
                object.put(PARAM_RESOLUTION_ID, Integer.valueOf(resolutionId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public JSONArray getProblemArray() {
        try {
            JSONArray problemArray = new JSONArray();
            for (ProblemResult problemResult : problem) {
                problemArray.put(problemResult.writeToJson());
            }
            return problemArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.problem);
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
        this.problem = in.createTypedArrayList(ProblemResult.CREATOR);
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
