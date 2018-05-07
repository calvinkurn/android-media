package com.tokopedia.tokocash.autosweepmf.data.model;
import com.google.gson.annotations.SerializedName;

public class ResponseAutoSweepDetail extends ResponseContainer {
    @SerializedName("data")
    private AutoSweepDetailEntity data;

    public AutoSweepDetailEntity getData() {
        return data;
    }

    public void setData(AutoSweepDetailEntity data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseAutoSweepDetail{" +
                "data=" + data +
                '}';
    }
}
