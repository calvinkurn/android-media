package com.tokopedia.profilecompletion.view.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by stevenfredian on 6/22/17.
 */

public class StepProgressBar extends ProgressBar{

    private static final int MAX_BAR = 10000;
    ProgressBarAnimation animation;

    public StepProgressBar(Context context) {
        super(context);
        init();
    }

    public StepProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StepProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StepProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setMax(MAX_BAR);
        animation = new ProgressBarAnimation(this);
    }

    @Override
    public synchronized void setProgress(int progress) {
        int newValue = progress * 100 ;
        if(animation == null){
            super.setProgress(progress);
        }else {
            animation.setValue(getProgress(), newValue);
            animation.setDuration(1000);
            this.startAnimation(animation);
            super.setProgress(newValue);
        }
    }

    public synchronized int getPercentProgress(){
        return super.getProgress() / MAX_BAR;
    }
}
