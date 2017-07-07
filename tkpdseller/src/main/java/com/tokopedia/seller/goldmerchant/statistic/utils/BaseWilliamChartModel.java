package com.tokopedia.seller.goldmerchant.statistic.utils;

/**
 * @author normansyahputa on 7/6/17.
 *         <p>
 *         this class tend to add more readability
 */

public class BaseWilliamChartModel {
    private String[] labels;
    private float[] values;

    public BaseWilliamChartModel(String[] labels, float[] values) {
        if (labels == null)
            throw new RuntimeException("unable to process null WilliamChartUtils mValues");
        if (values == null)
            throw new RuntimeException("unable to process null WilliamChartUtils mValues");

        this.labels = labels;
        this.values = values;
    }

    private BaseWilliamChartModel() {
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }
}
