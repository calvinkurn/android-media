package com.tokopedia.seller.shop.open.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.stepstone.stepper.internal.widget.StepTab;
import com.tokopedia.seller.R;

/**
 * Created by Nathaniel on 3/22/2017.
 */

public class TkpdStepTab extends StepTab {

    private View mStepDivider;
    private View mLeftStepDivider;

    public TkpdStepTab(Context context) {
        super(context);
        initializeView();
    }

    public TkpdStepTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public TkpdStepTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView();
    }

    private void initializeView() {
        mStepDivider = findViewById(R.id.ms_stepDivider);
        mLeftStepDivider = findViewById(R.id.ms_left_stepDivider);
    }

    @Override
    public void toggleDividerVisibility(boolean show) {
        mStepDivider.setVisibility(show ? VISIBLE : INVISIBLE);
    }

    public void toggleLeftDividerVisibility(boolean show) {
        mLeftStepDivider.setVisibility(show ? VISIBLE : INVISIBLE);
    }
}
