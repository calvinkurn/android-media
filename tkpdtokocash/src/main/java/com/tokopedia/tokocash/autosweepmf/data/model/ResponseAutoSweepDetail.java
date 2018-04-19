package com.tokopedia.tokocash.autosweepmf.data.model;

public class ResponseAutoSweepDetail extends ResponseContainer {
    private AutoSweepDetail data;

    public AutoSweepDetail getData() {
        return data;
    }

    public void setData(AutoSweepDetail data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseAutoSweepDetail{" +
                "data=" + data +
                '}';
    }
}
