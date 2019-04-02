package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

/**
 * @author by yfsx on 07/08/18.
 */
public class ComplaintResult implements Parcelable{
    public ProblemResult problem;
    public ProblemOrderResult order;
    public boolean isChecked;
    private static final String PARAM_PROBLEM = "problem";
    private static final String PARAM_ORDER = "order";

    public ComplaintResult() {
        this.problem = new ProblemResult();
        this.problem.id = -1;
        this.order = new ProblemOrderResult();
    }

    public JsonObject writeToJson() {
        JsonObject object = new JsonObject();
        try {
            if (problem != null) {
                object.add(PARAM_PROBLEM, problem.writeToJson());
            }
            if (order != null) {
                object.add(PARAM_ORDER , order.writeToJson());
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
        dest.writeParcelable(this.problem, flags);
        dest.writeParcelable(this.order, flags);
    }

    protected ComplaintResult(Parcel in) {
        this.problem = in.readParcelable(ProblemResult.class.getClassLoader());
        this.order = in.readParcelable(ProblemOrderResult.class.getClassLoader());
    }

    public static final Creator<ComplaintResult> CREATOR = new Creator<ComplaintResult>() {
        @Override
        public ComplaintResult createFromParcel(Parcel source) {
            return new ComplaintResult(source);
        }

        @Override
        public ComplaintResult[] newArray(int size) {
            return new ComplaintResult[size];
        }
    };
}
