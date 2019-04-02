package com.tokopedia.transaction.orders.orderlist.data.surveyrequest;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckBOMSurveyParams {

    @SerializedName("source")
    @Expose
    private String source;


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
