package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 18/08/17.
 */

public class ResultViewModel implements Parcelable {
    public List<ProblemResult> problem = new ArrayList<>();
    public int solution;
    public int refundAmount;
    public ProblemMessageResult message;
    public int attachmentCount;

    public JSONObject writeToJson() {
        JSONObject object = new JSONObject();
        try {
            if (problem.size() != 0) {
                JSONArray problemArray = new JSONArray();
                for (ProblemResult problemResult : problem) {
                    problemArray.put(problemResult.writeToJson());
                }
                object.put("problem", problemArray);
            }
            if (solution != 0) {
                object.put("solution", solution);
            }
            if (refundAmount != 0) {
                object.put("refundAmount", refundAmount);
            }
            if (attachmentCount != 0) {
                object.put("attachmentCount", attachmentCount);
            }
            if (message != null) {
                object.put("message", message.writeToJson());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.problem);
        dest.writeInt(this.solution);
        dest.writeInt(this.refundAmount);
        dest.writeParcelable(this.message, flags);
        dest.writeInt(this.attachmentCount);
    }

    public ResultViewModel() {
    }

    protected ResultViewModel(Parcel in) {
        this.problem = new ArrayList<ProblemResult>();
        in.readList(this.problem, ProblemResult.class.getClassLoader());
        this.solution = in.readInt();
        this.refundAmount = in.readInt();
        this.message = in.readParcelable(ProblemMessageResult.class.getClassLoader());
        this.attachmentCount = in.readInt();
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
