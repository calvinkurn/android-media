package com.tokopedia.inbox.rescenter.createreso.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

/**
 * Created by yoasfs on 18/08/17.
 */

public class ProblemOrderDetailResult implements Parcelable {
    public int id;

    public static final String PARAM_ID = "id";

    public JsonObject writeToJson() {
        JsonObject object = new JsonObject();
        try {
            if (id != 0) {
                object.addProperty(PARAM_ID, id);
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
        dest.writeInt(this.id);
    }

    public ProblemOrderDetailResult() {
    }

    protected ProblemOrderDetailResult(Parcel in) {
        this.id = in.readInt();
    }

    public static final Creator<ProblemOrderDetailResult> CREATOR = new Creator<ProblemOrderDetailResult>() {
        @Override
        public ProblemOrderDetailResult createFromParcel(Parcel source) {
            return new ProblemOrderDetailResult(source);
        }

        @Override
        public ProblemOrderDetailResult[] newArray(int size) {
            return new ProblemOrderDetailResult[size];
        }
    };
}
