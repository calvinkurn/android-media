package com.tokopedia.tokocash.autosweepmf.data.model;

public class ResponseAutoSweepDetail extends ResponseContainer {
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
