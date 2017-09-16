package com.tokopedia.seller.common.williamchart.base;

import java.util.Arrays;

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

    public BaseWilliamChartModel(BaseWilliamChartModel baseWilliamChartModel) {
        setValues(Arrays.copyOf(baseWilliamChartModel.getValues(), baseWilliamChartModel.getValues().length));
        setLabels(Arrays.copyOf(baseWilliamChartModel.getLabels(), baseWilliamChartModel.getLabels().length));
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

    public int size() {
        return values.length;
    }

    public void increment(int increment) {
        for (int i = 0; i < values.length; i++) {
            values[i] += increment;
        }
    }
}
