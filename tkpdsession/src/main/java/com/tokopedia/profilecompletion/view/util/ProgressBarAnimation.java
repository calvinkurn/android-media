package com.tokopedia.profilecompletion.view.util;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

/**
 * Created by stevenfredian on 6/19/17.
 */

public class ProgressBarAnimation extends Animation {
    private ProgressBar progressBar;
    private int max;
    private float from;
    private float to;

    public ProgressBarAnimation(ProgressBar progressBar) {
        super();
        this.progressBar = progressBar;
        this.max = progressBar.getMax();
    }

    public void setValue(int from, int to){
        this.from = from * max / 100;
        this.to = to * max / 100;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
    }

}
