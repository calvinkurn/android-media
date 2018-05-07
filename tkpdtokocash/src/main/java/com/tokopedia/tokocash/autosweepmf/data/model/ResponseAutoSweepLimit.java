package com.tokopedia.tokocash.autosweepmf.data.model;
import com.google.gson.annotations.SerializedName;

public class ResponseAutoSweepLimit extends ResponseContainer {
    @SerializedName("data")
    private AutoSweepLimitEntity data;

    public AutoSweepLimitEntity getData() {
        return data;
    }

    public void setData(AutoSweepLimitEntity data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseAutoSweepLimit{" +
                "data=" + data +
                '}';
    }
}
