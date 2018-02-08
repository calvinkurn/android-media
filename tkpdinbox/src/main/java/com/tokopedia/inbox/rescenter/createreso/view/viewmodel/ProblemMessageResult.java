package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by yoasfs on 18/08/17.
 */

public class ProblemMessageResult implements Parcelable {
    public String remark;

    public JSONObject writeToJson() {
        JSONObject object = new JSONObject();
        try {
            if (!remark.equals("")) {
                object.put("remark", remark);
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
        dest.writeString(this.remark);
    }

    public ProblemMessageResult() {
    }

    protected ProblemMessageResult(Parcel in) {
        this.remark = in.readString();
    }

    public static final Creator<ProblemMessageResult> CREATOR = new Creator<ProblemMessageResult>() {
        @Override
        public ProblemMessageResult createFromParcel(Parcel source) {
            return new ProblemMessageResult(source);
        }

        @Override
        public ProblemMessageResult[] newArray(int size) {
            return new ProblemMessageResult[size];
        }
    };
}
