package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

/**
 * Created by yoasfs on 18/08/17.
 */

public class ProblemOrderResult implements Parcelable {
    public ProblemOrderDetailResult detail;

    private static final String PARAM_DETAIL = "detail";

    public ProblemOrderResult() {
        detail = new ProblemOrderDetailResult();
    }

    public JsonObject writeToJson() {
        JsonObject object = new JsonObject();
        try {
            if (detail != null) {
                object.add(PARAM_DETAIL, detail.writeToJson());
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
        dest.writeParcelable(this.detail, flags);
    }

    protected ProblemOrderResult(Parcel in) {
        this.detail = in.readParcelable(ProblemOrderDetailResult.class.getClassLoader());
    }

    public static final Creator<ProblemOrderResult> CREATOR = new Creator<ProblemOrderResult>() {
        @Override
        public ProblemOrderResult createFromParcel(Parcel source) {
            return new ProblemOrderResult(source);
        }

        @Override
        public ProblemOrderResult[] newArray(int size) {
            return new ProblemOrderResult[size];
        }
    };
}
