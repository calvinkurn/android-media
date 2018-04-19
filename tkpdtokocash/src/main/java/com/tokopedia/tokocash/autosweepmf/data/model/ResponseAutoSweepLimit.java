package com.tokopedia.tokocash.autosweepmf.data.model;

public class ResponseAutoSweepLimit extends ResponseContainer {
    private AutoSweepLimit data;

    public AutoSweepLimit getData() {
        return data;
    }

    public void setData(AutoSweepLimit data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseAutoSweepLimit{" +
                "data=" + data +
                '}';
    }
}
