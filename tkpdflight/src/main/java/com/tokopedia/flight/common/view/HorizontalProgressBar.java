package com.tokopedia.flight.common.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.tokopedia.flight.R;

/**
 * Created by User on 11/20/2017.
 */

public class HorizontalProgressBar extends FrameLayout {

    private ProgressBar progressBar;

    public HorizontalProgressBar(@NonNull Context context) {
        super(context);
        init();
    }

    public HorizontalProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public HorizontalProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_horizontal_progress_bar, this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    public void setProgress(int progress) {
        if (Build.VERSION.SDK_INT >= 24) {
            progressBar.setProgress(progress, true);
        } else {
            progressBar.setProgress(progress);
        }
    }
}
